library(ggplot2); library(stringr); library(gridExtra); library(pastecs)

removeMS <- function(x)
{
  x <- as.character(x)
  for(i in 1:length(x))
  {
    x[i] <- str_replace(x[i], " ms", "")
  }
  return(x)
}

KnapsackRes <- na.omit(res)
KnapsackRes$DP1.Running.Time <- as.numeric(removeMS(KnapsackRes$DP1.Running.Time))
KnapsackRes$DP2.Running.Time <- as.numeric(removeMS(KnapsackRes$DP2.Running.Time))
KnapsackRes$Greedy.Running.Time <- as.numeric(removeMS(KnapsackRes$Greedy.Running.Time))
KnapsackRes$FPTAS.Running.Time <- as.numeric(removeMS(KnapsackRes$FPTAS.Running.Time))

summary(KnapsackRes)

## Save this dataframe for report
save(KnapsackRes, file="KnapsackRes.RData")

## Statistical Summary
stat.desc(KnapsackRes$DP1.Running.Time, basic = F)
stat.desc(KnapsackRes$DP2.Running.Time, basic = F)
stat.desc(KnapsackRes$Greedy.Running.Time, basic = F)
stat.desc(KnapsackRes$FPTAS.Running.Time, basic = F)

## Facets of running time
dp1_rt <- ggplot(KnapsackRes, aes(count, DP1.Running.Time)) + geom_point() + geom_smooth()
dp2_rt <- ggplot(KnapsackRes, aes(count, DP2.Running.Time)) + geom_point() + geom_smooth()
greedy_rt <- ggplot(KnapsackRes, aes(count, Greedy.Running.Time)) + geom_point() + geom_smooth()
fptas_rt <- ggplot(KnapsackRes, aes(count, FPTAS.Running.Time)) + geom_point() + geom_smooth()
grid.arrange(dp1_rt, dp2_rt, greedy_rt, fptas_rt)

## spread out of running time
ggplot(KnapsackRes) + 
  geom_point(aes(x=as.factor(count), y=DP1.Running.Time, colour = "DP1.Running.Time"), alpha = 0.7) +
  geom_point(aes(x=as.factor(count), y=DP2.Running.Time, colour = "DP2.Running.Time"), alpha = 0.7) + 
  geom_point(aes(x=as.factor(count), y=Greedy.Running.Time, colour = "Greedy.Running.Time"), alpha = 0.7) + 
  geom_point(aes(x=as.factor(count), y=FPTAS.Running.Time, colour = "FPTAS.Running.Time"), alpha = 0.7) +
  xlab("Instances") + 
  ylab("RunningTime")

## Density of running time
ggplot(KnapsackRes) +
  geom_density(aes(DP1.Running.Time, colour="DP1.Running.Time")) + 
  geom_density(aes(DP2.Running.Time, colour="DP2.Running.Time")) + 
  geom_density(aes(Greedy.Running.Time, colour="Greedy.Running.Time")) + 
  geom_density(aes(FPTAS.Running.Time, colour = "FPTAS.Running.Time")) +
  xlab("Running Time") + 
  xlim(0, 10)
