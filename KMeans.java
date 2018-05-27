/*** Author :Vibhav Gogate
 *           Apurva Mithal
The University of Texas at Dallas
 *****/

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class KMeans {
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out
					.println("Usage: Kmeans <input-image> <k> <output-image>");
			return;
		}
		try {
			BufferedImage originalImage = ImageIO.read(new File(args[0]));
			int k = Integer.parseInt(args[1]);
			BufferedImage kmeansJpg = kmeans_helper(originalImage, k);
			ImageIO.write(kmeansJpg, "jpg", new File(args[2]));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private static BufferedImage kmeans_helper(BufferedImage originalImage,
			int k) {
		int w = originalImage.getWidth();
		int h = originalImage.getHeight();
		System.out.println("w : " + w);
		System.out.println("h : " + h);

		BufferedImage kmeansImage = new BufferedImage(w, h,
				originalImage.getType());
		Graphics2D g = kmeansImage.createGraphics();
		g.drawImage(originalImage, 0, 0, w, h, null);
		// Read rgb values from the image
		int[] rgb = new int[w * h];
		int count = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				rgb[count++] = kmeansImage.getRGB(i, j);
			}
		}
		// Call kmeans algorithm: update the rgb values
		kmeans(rgb, k);

		// Write the new rgb values to the image
		count = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				kmeansImage.setRGB(i, j, rgb[count++]);
			}
		}
		return kmeansImage;
	}

	// Your k-means code goes here
	// Update the array rgb by assigning each entry in the rgb array to its
	// cluster center

	private static void kmeans(int[] rgb, int k) {
		// randomly select centroids
		// assign each point to a centroid
		// find new set of centroids
		// continue till convergence
		// feature set consists of 4 attributes - Red, Blue, Green, Alpha

		int[] centroid = new int[k];
		int[] assignment = new int[rgb.length];
		int[] alphaSum = new int[k];
		int[] redSum = new int[k];
		int[] greenSum = new int[k];
		int[] blueSum = new int[k];
		int[] totalAssignments = new int[k];
		int[] old_assignment = new int[rgb.length];

		Random rand = new Random();

		centroid[0] = rgb[rand.nextInt(rgb.length)];
		for (int i = 1; i < k; i++) {
			boolean duplicate = true;
			do {
				int randomNum = rand.nextInt(rgb.length);
				for (int j = 0; j < i; j++) {
					if (j == i - 1 && (centroid[j] != rgb[randomNum])) {
						duplicate = false;
						centroid[i] = rgb[randomNum];
					} else if (rgb[randomNum] == centroid[j]) {
						j = i;
					}
				}
			} while (duplicate);
		}

		int alpha_dp = 0;
		int alpha_c = 0;
		int red_dp = 0;
		int red_c = 0;
		int green_dp = 0;
		int green_c = 0;
		int blue_dp = 0;
		int blue_c = 0;
		System.out.println("k " + k);
		// boolean didConverge = false;
		int iter = 0;
		int maxIter = 100;

		while (iter < maxIter) {
			for (int i = 0; i < k; i++) {
				alphaSum[i] = 0;
				redSum[i] = 0;
				greenSum[i] = 0;
				blueSum[i] = 0;
				totalAssignments[i] = 0;
			}

			for (int dataPoint = 0; dataPoint < rgb.length; dataPoint++) {
				double minDist = Double.MAX_VALUE;
				int cluster = 0;
				alpha_dp = (rgb[dataPoint] >> 24) & 0xFF;
				red_dp = (rgb[dataPoint] >> 16) & 0xFF;
				green_dp = (rgb[dataPoint] >> 8) & 0xFF;
				blue_dp = (rgb[dataPoint] >> 0) & 0xFF;

				for (int center = 0; center < k; center++) {
					alpha_c = (centroid[center] >> 24) & 0xFF;
					red_c = (centroid[center] >> 16) & 0xFF;
					green_c = (centroid[center] >> 8) & 0xFF;
					blue_c = (centroid[center] >> 0) & 0xFF;

					int alphaDist = alpha_dp - alpha_c;
					int redDist = red_dp - red_c;
					int greenDist = green_dp - green_c;
					int blueDist = blue_dp - blue_c;
					double distance = Math.sqrt((alphaDist * alphaDist)
							+ (redDist * redDist) + (greenDist * greenDist)
							+ (blueDist * blueDist));
					if (distance < minDist) {
						minDist = distance;
						cluster = center;
					}
				}
				assignment[dataPoint] = cluster;
				totalAssignments[cluster] += 1;
				alphaSum[cluster] += alpha_dp;
				redSum[cluster] += red_dp;
				blueSum[cluster] += blue_dp;
				greenSum[cluster] += green_dp;
			}

			for (int i = 0; i < k; i++) {
				centroid[i] = ((((int) ((double) alphaSum[i] / (double) totalAssignments[i])) & 0x000000FF) << 24)
						| ((((int) ((double) redSum[i] / (double) totalAssignments[i])) & 0x000000FF) << 16)
						| ((((int) ((double) greenSum[i] / (double) totalAssignments[i])) & 0x000000FF) << 8)
						| ((((int) ((double) blueSum[i] / (double) totalAssignments[i])) & 0x000000FF) << 0);
			}

			/*
			 * int flag = 0; for(int i=0; i<rgb.length; i++){
			 * if(old_assignment[i] != assignment[i]){ flag = 1; break; } }
			 * if(flag ==1){ for( int i=0; i<k; i++){ centroid[i] =
			 * ((((int)((double)alphaSum[i] / (double)totalAssignments[i])) &
			 * 0x000000FF) << 24) | ((((int)((double)redSum[i] /
			 * (double)totalAssignments[i])) & 0x000000FF) << 16) |
			 * ((((int)((double)greenSum[i] / (double)totalAssignments[i])) &
			 * 0x000000FF) << 8) | ((((int)((double)blueSum[i] /
			 * (double)totalAssignments[i])) & 0x000000FF) << 0); } } else
			 * didConverge = true;
			 */
			iter++;
		}

		for (int i = 0; i < rgb.length; i++) {
			rgb[i] = centroid[assignment[i]];
		}

	}

}
