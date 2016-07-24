README for Assignment 2

I have submitted assignment in done in 2 languages : R and Java.
Instruction to run R and Java program is given below.

-------------------------------------------------------------------------------------------
FILES ATTACHED FOR R_Programming in folder "R"
1.	Rscript named – “shah_sas151830_command_line.R” to run Rscript from command line
2.	Rplots_set1.pdf and Rplots_set2.pdf for plots generated
3.	Report file named "R_Report.pdf" showing accuracy for the both datasets.


FILES ATTACHED FOR JAVA CODE in folder "JAVA"
1.	folder named "scr" inside folder JAVA (JAVA\scr)
2.	Report file named "Java_report.pdf" answering following to questions:
	- Report the accuracy on the test set for decision trees constructed
	- Choose 10 suitable values for L and K. For each of them, report the accuracies for the post-
	  pruned decision trees constructed


======== RSCRIPT Readme Instructions =====================================================

------------------------------------------------------------------------------------
Packages used in Rscripts:
1.	Rpart – for rpart library
2.	rpart.plot – for rpart.plot library
------------------------------------------------------------------------------------
Steps to run Rscripts from command line:
FILES ARE PLACED IN FOLDER "R"
1.	Place file named – “shah_sas151830_command_line.R” in your working directory. Also place training, test and validation files at the same location.
2.	Command to run rscript from command line:
-	rscript shah_sas151830_command_line.R training_set.csv validation_set.csv test_set.csv
3.	The packages will take time to install. Wait for a while.
4.	A Rplots.pdf is generated in the location of the workspace (also attached separate file already generated as Rplots_set1.pdf and Rplots_set2.pdf)
5.	The output is in accordance with the question of assignment 2.

------------------------------------------------------------------------------------
Report file : 
- Please find report file named report_R.pdf for accuracy counts asked.

------------------------------------------------------------------------------------
Accuracy: 

For data set 1- 73.55% (Upto 2 decimal point accuracy)
For data set 2- 72.67% (Upto 2 decimal point accuracy)


=============== JAVA Code Readme instructions ====================================================

------------------------------------------------------------------------------------
Steps to run java code:

1.	Navigate upto scr folder where Main.java is kept (JAVA/scr/Main.java)
2.	You need to put Training_set.csv, Test_set.csv and Validation_set.csv in same folder on which you want to test the program. (I have kept training_set.csv, 		validation_set.csv and test_set.csv from data_set1 already there, in case if you require another set of files place them there.)
2.	Run following command from cmd :
-	Javac Main.java
-	Java Main <interger L> <integer K> <training-set> <validation-set> <test-set> <to-print>
	EXAMPLE : Java Main 8 6 training_set.csv validation_set.csv test_set.csv yes
3.	File named output.txt will be generated at the same location to show Decision tree given <to-print>, and accuracy of Main and Pruned tree accuracy. 
------------------------------------------------------------------------------------

