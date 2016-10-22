# install.packages('e1071', dependencies = TRUE) 

library (e1071)

sum = 0
count =0

for(i in 1:10){
  data1 = read.table("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data", sep=",")
  
  # sampling data to training and test dataset
  sample_data<- sample(nrow(data1), size = 0.90*nrow(data1))
  train_data<-data1[sample_data,]
  test_data <- data1[-sample_data,]
  
  #Modeling data, predicting based on test data and finding accuracy
  model <- naiveBayes(as.factor(train_data[,9])~.,data = train_data[,1:8])
  pred = predict(model,test_data)
  table_dim <- table(test_data[,9], pred) 
  acc = sum(table_dim[row(table_dim)==col(table_dim)])/sum(table_dim)
  cat("Experiment: ",i,"Accuracy",acc*100,"%\n")
  
  sum = sum+acc
  count = count +1
}
avg = sum/count
cat("AVerage Accuracy is:",avg*100)
