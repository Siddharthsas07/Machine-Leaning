The folder “Assignment 3” contains 4 folders :

Q1ExploratoryDataAnalysis
Q2NaïveBayesClassifier
Q3SVMClassifier
Q4kNN

Each folder contains following two items:

1. Report file for given section
2. rScript for given section

===================================================================
HOW TO RUN

1. Open COMMAND PROMPT
2. Run following commands to run above scripts:

I. q1_Exploratory_Data_Analysis.R
- Navigate upto folder Q1ExploratoryDataAnalysis
- run command -> rscript q1_Exploratory_Data_Analysis.R

II. q2_Naive_bayes.R
- Navigate upto folder Q2NaïveBayesClassifier
- run command -> rscript q2_Naive_bayes.R

III. q3_svm_Classifier.R
- Navigate upto folder Q3SVMClassifier
- run command -> rscript q3_svm_Classifier.R

IV. q4_knn.R
- Navigate upto folder Q4kNN
- run command -> rscript q4_knn.R

======================================================================================
Q. Which method works the best for this dataset?
A. Observing the output of rscripts following can be determined for given dataset:

- kNN has least average accuracy, so that is least preferable method to analyze given dataset.
- svm has the best average accuracy(between 75-77) for given dataset, so svm is perfect classifier for classification of this dataset.
- Naive Bayesian also have average accuracy around 74-75, but it deviates lot from experiment to experiment. So SVM is more reliable for classification of this dataset.
