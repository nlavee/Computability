library(ggplot2); library(stringr)

removeMS <- function(x)
{
  x <- as.character(x)
  for(i in 1:length(x))
  {
    x[i] <- str_replace(x[i], " ms", "")
  }
  return(x)
}

KnapsackRes <- na.omit(KnapsackRes)
KnapsackRes$DP1.Running.Time <- as.numeric(removeMS(KnapsackRes$DP1.Running.Time))
KnapsackRes$DP2.Running.Time <- as.numeric(removeMS(KnapsackRes$DP2.Running.Time))
KnapsackRes$Greedy.Running.Time <- as.numeric(removeMS(KnapsackRes$Greedy.Running.Time))
KnapsackRes$FPTAS.Running.Time <- as.numeric(removeMS(KnapsackRes$FPTAS.Running.Time))


summary(KnapsackRes)

ggplot(KnapsackRes) + 
  geom_point(aes(x=as.factor(count), y=DP1.Running.Time, colour = "DP1.Running.Time"), alpha = 0.2) + 
  geom_point(aes(x=as.factor(count), y=DP2.Running.Time, colour = "DP2.Running.Time"), alpha = 0.2) + 
  geom_point(aes(x=as.factor(count), y=Greedy.Running.Time, colour = "Greedy.Running.Time"), alpha = 0.2) + 
  geom_point(aes(x=as.factor(count), y=FPTAS.Running.Time, colour = "FPTAS.Running.Time"), alpha = 0.2) +
  xlab("Instances") + 
  ylab("RunningTime")

ggplot(KnapsackRes) +
  geom_density(aes(DP1.Running.Time, colour="DP1.Running.Time")) + 
  geom_density(aes(DP2.Running.Time, colour="DP2.Running.Time")) + 
  geom_density(aes(Greedy.Running.Time, colour="Greedy.Running.Time")) + 
  geom_density(aes(FPTAS.Running.Time, colour = "FPTAS.Running.Time")) +
  xlab("Running Time") + 
  xlim(55, 200)
