# install.packages('e1071', dependencies = TRUE) 

library(class)
library(e1071)

myData = read.csv("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data", header = FALSE)

# Rename the columns
names(myData) = c("PregnancyNumber","PlasmaGlucose","BP","Triceps","Insulin","BMI","DiabetesFunction","Age","Class")
str(myData)

sum = 0
count =0

k_values = c(3,5,7,9,11)

for(j in k_values){
  cat ("Value of K is", j, "\n")

for(i in 1:10)
{
  
  # sampling data to training and test dataset
  indexes = sample(1:nrow(myData), size=0.9*nrow(myData))
  pima_test = myData[-indexes,]
  pima_train = myData[indexes,]

  #Modeling data, predicting based on test data and finding accuracy
  knn_data = knn(pima_train, pima_test, cl = pima_train$Class, k = j)
  myTable = table(pima_test$Class,knn_data)
  accuracy = sum(myTable[row(myTable)==col(myTable)])/sum(myTable)*100
  
  sum = sum+accuracy
  count = count +1
  
}
  avg = sum/count
  cat ("Overall AVerage Accuracy is:", avg, "% for kNN = ", j ,"\n\n")
}