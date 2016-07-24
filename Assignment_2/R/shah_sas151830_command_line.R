args <- commandArgs(trailingOnly = TRUE)

install.packages("rpart.plot", repos="http://cran.rstudio.com/", dependencies=TRUE)

#Loading the library
library(rpart)
library(rpart.plot)

#reading training and test data set
data_train = read.csv(args[1])
data_validate = read.csv(args[2])
data_test = read.csv(args[3])

#Create	a	model	using	the	rpart	function
treemodel<-rpart(Class~.,data=data_train, method='class', parms=list(split='information'), minsplit=1, minbucket=1)
print(treemodel)

#Printing summary of the model
printcp(treemodel)

#Ploting decision	tree 
par(mar = rep(0.1, 4))
plot(treemodel)
text(treemodel)

#Plot of summary of model
plotcp(treemodel)

#Using the best pruning parameter to create a pruned	tree.
prunedTree<-prune(treemodel, cp= treemodel$cptable[which.min(treemodel$cptable[,"xerror"]),"CP"])

#ploting pruned tree
par(mar=c(5.1,4.1,4.1,2.1))
prp(prunedTree, uniform = TRUE, main="Pruned decision tree for Training Data")

#Plot of summary of model
plotcp(prunedTree)

#Predicting based on the test data 
test_data<-predict(prunedTree, data_test, type=c("class"))
summary(test_data)

# calculating the accuracy
accuracy_test <- sum(test_data==data_test$Class)/nrow(data_test)
cat("Accuracy for test data is:", accuracy_test)

