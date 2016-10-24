# install.packages("rpart.plot", repos="http://cran.rstudio.com/", dependencies=TRUE)
# install.packages('rpart', dependencies=TRUE)
# install.packages('randomForest', dependencies=TRUE)
# install.packages('e1071', dependencies=TRUE)
# install.packages('aod', dependencies = TRUE) 
# install.packages('neuralnet', dependencies=T)
# install.packages("neuralnet",dependencies=T)
# install.packages('adabag',dependencies=T)
# install.packages("ada",dep = T)
# install.packages('ggplot2', dep = TRUE)
# install.packages("ipred",dependencies = T)
# update.packages()

library(rpart)
library(rpart.plot)
library(class)
library(e1071)
library(aod)
library(mlbench)
library(adabag)
library(randomForest)
library(ada)
library(ipred)
library("neuralnet")
library(ggplot2)
require(adabag)

args <- commandArgs(TRUE)

daturlarg = arg[1]
dataURL<-as.character(daturlarg)
header<-as.logical(arg[2])
arg3 = args[3]
d<-read.csv(dataURL,header = header)
d[ is.na(d) ] <- 0
d[d == "?"] <- 0

#d <- d[-grep("?",d),]
# create 10 samples
set.seed(123)

avg = c(0,0,0,0,0,0,0,0,0)
sum = c(0,0,0,0,0,0,0,0,0)

for(i in 1:10) {
  
  cat("Running sample ",i,"\n")
  sampleInstances<-sample(1:nrow(d),size = 0.9*nrow(d))
  trainingData<-d[sampleInstances,]
  testData<-d[-sampleInstances,]
  
  # which one is the class attribute
  
  if(daturlarg=='http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wpbc.data'){
    Class<-as.factor(trainingData[,arg3])
  }else if(daturlarg=='http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.data'){
    Class<-as.factor(trainingData[,arg3])
  }else if(daturlarg=='http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data'){
    Class<-as.factor(trainingData[,arg3])
  }else {
    Class<-as.factor(as.integer(trainingData[,arg3]))
  }
  
  #----------Decision Tree-------------
  treemodel <- rpart(Class ~ ., data = trainingData[,-arg3], method = "class")
  predictedvalue <- predict(treemodel, testData[,-arg3], type = "class")
  accuracy_tree = sum(testData[,arg3] == predictedvalue)/length(predictedvalue)*100
  # str(predictedvalue)
  cat("Method = ", "Decision Tree",", accuracy= ", accuracy_tree,"\n")
  
  
  #-----------SVM--------------
  model_svm <- svm(Class ~ ., data = trainingData[,-arg3])
  predicted_svm <- predict(model_svm,testData[,-arg3])
  accuracy_svm  <- sum(predicted_svm == testData[,arg3])/length(predicted_svm)*100
  cat("Method = ", "SVM",", accuracy= ", accuracy_svm,"\n")
  
  #-----------Naïve Bayesian--------
  model_naive = naiveBayes(Class~., data = trainingData[,-arg3])
  predicted_naive <- predict(model_naive,testData[-arg3])
  accuracy_naive <- sum(predicted_naive == testData[,arg3])/length(predicted_naive)*100
  cat("Method = ", "Naïve Bayesian",", accuracy= ", accuracy_naive,"\n")
  
  
  #-----------kNN----------
  predicted_knn <- knn(trainingData[,-arg3], testData[,-arg3],Class, k =9)
  #To find the accuracy:
  accuracy_knn <- sum(predicted_knn == testData[,arg3])/length(predicted_knn)*100
  cat("Method = ", "Knn",", accuracy= ", accuracy_knn,"\n")
  
  #-----------Logistic Regression
  #trainingData$rank<- factor(trainingData$rank)
  #testData$rank<- factor(testData$rank)
  
  if(daturlarg=='http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data'){
    logisticmodel <- glm(Class ~ ., data = trainingData[,-arg3],family = "binomial")
    predicted_reg <- predict(logisticmodel, newdata = testData[,-arg3], type = "response")
    
    threshold=0.7 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_reg, FUN=function(x) if (x>threshold) 'g' else 'b')
    actual<-testData[,arg3]
    accuracy_reg <- sum(actual==prediction)/length(prediction)*100
    cat("Method = ", "logistic regression",", accuracy= ", accuracy_reg,"\n")
    
  }else if(daturlarg=="http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.data"){
    logisticmodel <- glm(Class ~ ., data = trainingData[,-arg3],family = "binomial")
    predicted_reg <- predict(logisticmodel, newdata = testData[,-arg3], type = "response")
    
    threshold=0.7 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_reg, FUN=function(x) if (x>threshold) 'M' else 'B')
    actual<-testData[,arg3]
    accuracy_reg <- sum(actual==prediction)/length(prediction)*100
    cat("Method = ", "logistic regression",", accuracy= ", accuracy_reg,"\n")
    
  }else if(daturlarg=="http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wpbc.data"){
    logisticmodel <- glm(Class ~ V1+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16+V17+V18+V19+V20+V21+V22+V23+V24+V25+V26+V27+V28+V29+V30+V31+V32+V33+V34, data = trainingData[,-arg3],family = "binomial")
    predicted_reg <- predict(logisticmodel, newdata = testData[,-arg3], type = "response")
    
    threshold=0.7 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_reg, FUN=function(x) if (x>threshold) 'R' else 'N')
    actual<-testData[,arg3]
    accuracy_reg <- sum(actual==prediction)/length(prediction)*100
    cat("Method = ", "logistic regression",", accuracy= ", accuracy_reg,"\n")
  }else{
    logisticmodel <- glm(Class ~ ., data = trainingData[,-arg3],family = "binomial")
    # confint(logisticmodel)
    predicted_reg <- predict(logisticmodel, newdata = testData[,-arg3], type = "response")
    
    threshold=0.7 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_reg, FUN=function(x) if (x>threshold) 1 else 0)
    actual<-testData[,arg3]
    accuracy_reg <- sum(actual==prediction)/length(prediction)*100
    cat("Method = ", "logistic regression",", accuracy= ", accuracy_reg,"\n")
  }
  

  
  #-----------Bagging
  model <- bagging(Class ~ ., data=trainingData[,-arg3], coob=TRUE)
  p=predict(model,testData[,-arg3])
  # accuracy
  accuracy_bag <- sum(testData[,arg3]==p)/length(p)*100
  cat("Method = ", "Bagging",", accuracy= ", accuracy_bag,"\n")
  
  
  #-----------Boosting
  model <- ada(Class ~ ., data = trainingData[,-arg3], iter=20, nu=1, type="discrete")
  p=predict(model,testData[,-arg3])
  # accuracy
  accuracy_boost <- sum(testData[,arg3]==p)/length(p)*100
  cat("Method = ", "Boosting",", accuracy= ", accuracy_boost,"\n")
  
  #-----------Neural Network
  #1
  
  if(daturlarg=='http://www.utdallas.edu/~axn112530/cs6375/creditset.csv'){
    neuralModel <- neuralnet(default10yr ~clientid+income+age+loan+LTI , trainingData, hidden = 4, lifesign = "minimal", 
                             linear.output = FALSE, threshold = 0.1)
    predicted_labels <- compute(neuralModel, testData[,-arg3])
    threshold=0.3000 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_labels$net.result, FUN=function(x) if (x>threshold) 1 else 0)
    accuracy_net <- sum(actual==prediction)/length(prediction)*100
    cat("Method = ", "Neural Network",", accuracy= ", accuracy_net,"\n")     
  }
  
  #2
  if(daturlarg=='http://www.ats.ucla.edu/stat/data/binary.csv'){
    neuralModel <- neuralnet(admit ~ gre +gpa+rank , trainingData, hidden = 4, lifesign = "minimal", 
                             linear.output = FALSE, threshold = 0.1)
    predicted_labels <- compute(neuralModel, testData[,-arg3])
    threshold=0.3000                                                                                                                 
    prediction<-sapply(predicted_labels$net.result, FUN=function(x) if (x>threshold) 1 else 0)
    accuracy_net <- sum(actual==prediction)/length(prediction)*100
    cat("Method = ", "Neural Network",", accuracy= ", accuracy_net,"\n")     
  }
  #3
  if(daturlarg=="http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.data"){
    trainingData$V2<-sapply(trainingData$V2, FUN=function(x) if (x=='B') 1 else 0)
    testData$V2<-sapply(testData$V2, FUN=function(x) if (x=='B') 1 else 0)
    neuralModel <- neuralnet(V2 ~V1+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16+V17+V18+V19+V20+V21+V22+V23+V24+V25+V26+V27+V28+V29+V30+V31+V32 , trainingData, hidden = 4, lifesign = "minimal", 
                             linear.output = FALSE, threshold = 0.1)
    predicted_labels <- compute(neuralModel, testData[,-arg3])
    threshold=0.3000 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_labels$net.result, FUN=function(x) if (x>threshold) 1 else 0)
    accuracy_net <- sum(testData$V2==prediction)/length(prediction)*100
    cat("Method = ", "Neural Network",", accuracy= ", accuracy_net,"\n")  
  }
  #4
  if(daturlarg=="http://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wpbc.data"){
    trainingData$V2<-sapply(trainingData$V2, FUN=function(x) if (x=='N') 1 else 0)
    testData$V2<-sapply(testData$V2, FUN=function(x) if (x=='N') 1 else 0)
    temp = testData[,-35]
    #testData$V2<-sapply(testData$V35, FUN=function(x) if (x=='g') 1 else 0)
    neuralModel <- neuralnet(V2 ~V1+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16+V17+V18+V19+V20+V21+V22+V23+V24+V25+V26+V27+V28+V29+V30+V31+V32+V33+V34 , trainingData, hidden = 4, lifesign = "minimal",linear.output = FALSE, threshold = 0.1)
    predicted_labels <- compute(neuralModel, temp[,-arg3])
    threshold=0.3000 # put a threshold inorder to get the maximum accuracy                                                                                                                 
    prediction<-sapply(predicted_labels$net.result, FUN=function(x) if (x>threshold) 1 else 0)
    accuracy_net <- sum(temp$V2==prediction)/length(prediction)*100
    cat("Method = ", "Neural Network",", accuracy= ", accuracy_net,"\n")  
  } 
  #5
  if(daturlarg=='http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data'){
    trainingData$V35<-sapply(trainingData$V35, FUN=function(x) if (x=='g') 1 else 0)
    testData$V35<-sapply(testData$V35, FUN=function(x) if (x=='g') 1 else 0)
    
    neuralModel <- neuralnet(V35 ~V1+V2+V3+V4+V5+V6+V7+V8+V9+V10+V11+V12+V13+V14+V15+V16+V17+V18+V19+V20+V21+V22+V23+V24+V25+V26+V27+V28+V29+V30+V31+V32+V33+V34 , trainingData, hidden = 4, lifesign = "minimal",linear.output = FALSE, threshold = 0.1)
    predicted_labels <- compute(neuralModel, testData[,-arg3])
    threshold=0.3000                                                                                                                 
    prediction<-sapply(predicted_labels$net.result, FUN=function(x) if (x>threshold) 1 else 0)
    accuracy_net <- sum(testData$V35==prediction)/length(prediction)*100
    cat("Method = ", "Neural Network",", accuracy= ", accuracy_net,"\n")  
  }  
  
  
  #-----------Random Forest
  #fit <- randomForest(Class ~.,   data=trainingData[,-arg3], importance=TRUE,proximity=TRUE)
  fit <- randomForest(Class ~ ., data=trainingData[,-arg3], mtry=3,importance=TRUE, na.action=na.omit)
  pred <- predict(fit, newdata=testData[,-arg3],OOB=TRUE, type = "response")
  accuracy_forest <- sum(actual==pred)/length(pred)*100
  cat("Method = ", "Random Forest",", accuracy= ", accuracy_forest,"\n")
  
  
  
  #-----------------------
  cat("\n")
}
