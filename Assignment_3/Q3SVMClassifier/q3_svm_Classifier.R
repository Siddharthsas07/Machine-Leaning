# install.packages('e1071', dependencies = TRUE) 

library(class)
library (e1071)

sum = 0
count =0
flag_default = 0

myData = read.csv("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data", header = FALSE)

# Rename the columns
names(myData) = c("PregnancyNumber","PlasmaGlucose","BP","Triceps","Insulin","BMI","DiabetesFunction","Age","Class")
str(myData)

svm_values = c('', 'linear', 'polynomial', 'radial', 'sigmoid')

for(j in svm_values)
{
for(i in 1:10)
{
  # sampling data to training and test dataset
  indexes = sample(1:nrow(myData), size=0.9*nrow(myData))
  test = myData[-indexes,]
  train = myData[indexes,]
  
  train$Class = as.factor(train$Class)
  test$Class = as.factor(test$Class)
  if (flag_default == 0){
    svmModel = svm(train$Class ~ ., data = train)
    p = predict(svmModel,test)
    myTable = table(test$Class,predicted = p)
    accuracy = (sum(myTable[row(myTable)==col(myTable)])/sum(myTable))*100
    cat ("Experiment: ",i,"Accuracy",accuracy,"%\n")
  }else{
    svmModel = svm(train$Class ~ ., data = train, kernel = j)
    p = predict(svmModel,test)
    myTable = table(test$Class,predicted = p)
    accuracy = (sum(myTable[row(myTable)==col(myTable)])/sum(myTable))*100
  }
  
  sum = sum+accuracy
  count = count +1
}
  
  avg = sum/count
  if (flag_default == 0){
    cat ("\nOverall AVerage Accuracy is:", avg,"% for kernal = default \n\n")
    flag_default = 1
  }else{
    cat ("Overall AVerage Accuracy is:", avg, "% for kernal =",j,"\n")
  }
  
}