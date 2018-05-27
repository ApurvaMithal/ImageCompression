# ImageCompression

Apurva Mithal
axm174531
CS 6375.001
			                                       
-	Implemented K-means clustering on images

-	System Details
	-	Operating System: Windows 10
	-	Java 7 (jdk1.7.0_80)

-	File Names:
	-	KMeans.java – Code for K-means clustering on images

-	How to run the code

	-	Open the command prompt in Windows.
	-	Go to the path where the java file and the original images (Penguins.jpg and Koala.jpg) are present. 
	-	Compile Kmeans.java by giving the following commands:
		-	javac KMeans.java
	-	Run Kmeans.java by giving the following commands:
		-	java KMeans <input-image-name> <k> <output-image-name>
		-	eg. 
			java KMeans Penguins.jpg 2 Penguins_Output_K2.jpg
			java KMeans Koala.jpg 2 Koala_Output_K2.jpg

	-	Results folder contains the report (compression readings and answers) and the subfolder Output_Images which contains the Output Images.
	-	Output images are named as <<OriginalImage>_Output_K<Value>>
	-	Here, OutputImage is the name of the Original Image as in Penguins or Koala, Value is the value of the k in kmeans algorithm. Eg. Koala_Output_K2


