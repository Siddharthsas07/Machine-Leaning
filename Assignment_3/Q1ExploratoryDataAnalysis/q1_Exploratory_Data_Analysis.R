data1 = read.csv("https://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data", header= FALSE, sep=",")
names(data1) = c("PregnancyNumber","PlasmaGlucose","BP","Triceps","Insulin","BMI","DiabetesFunction","Age","Class")
str(data1)

#Step 1: Create the following plots: histogram, and barplot
i=1
for(i in 1:8){
  hist(data1[,i], main=paste("Histogram of attribute",names(data1[i])), col="blue",
       ylab = "FREQUENCY", xlab = names(data1[i]))
}

for(i in 1:8){
  barplot(data1[,i], main=paste("Barplot of attribute",names(data1[i])), col="red",
          ylab = "FREQUENCY", xlab = names(data1[i]))
}

#Step 2: Find the correlation between each of the attributes and the class variable
i=1
max_val = -1
index = -1
for(i in 1:8){
  temp = cor(data1[,i],data1[,9])
  cat("\n Correlation of atrributes", i, names(data1[i]), "with class is: ", temp)
  if(max_val < temp){
    max_val = temp
    index = i
  }
}
cat("\nmaximim correlation is :",max_val, "for attribute", names(data1[index]))

#Step 3 Compute the correlation between all pairs of the 8 attributes. Which two attributes have the highest mutual correlation?
i=1
j=1
max_val2 = -1
index_1 = -1
index_2 = -1
for(i in 1:8){
  for(j in i:8){
    if(i!=j){
      temp2 = cor(data1[,i],data1[,j])
      # Sys.sleep(0.1)
      cat("\n Correlation of atrributes", i, names(data1[i]), "and", 
          j,names(data1[j]), "is: ", temp2)
      if(max_val2 < temp2){
        max_val2 = temp2;
        index_1 = i
        index_2 = j
      }
    }
  }
}
cat("\nmaximim correlation is :",max_val2, "between ", names(data1[index_1]),"and ", names(data1[index_2]))

