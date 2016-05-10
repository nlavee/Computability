library(ggplot2); library(stringr); library(gridExtra); library(pastecs); library(dplyr)

removeMS <- function(x)
{
  x <- as.character(x)
  for(i in 1:length(x))
  {
    x[i] <- str_replace(x[i], " ms", "")
  }
  return(x)
}

#### KNAPSACK EXPERIMENTS ####

KnapsackRes <- na.omit(knapsack_res)
KnapsackRes$DP1.Running.Time <- as.numeric(removeMS(KnapsackRes$DP1.Running.Time))
KnapsackRes$DP2.Running.Time <- as.numeric(removeMS(KnapsackRes$DP2.Running.Time))
KnapsackRes$Greedy.Running.Time <- as.numeric(removeMS(KnapsackRes$Greedy.Running.Time))
KnapsackRes$FPTAS.Running.Time <- as.numeric(removeMS(KnapsackRes$FPTAS.Running.Time))

summary(KnapsackRes)

## Save this dataframe for report
save(KnapsackRes, file="/Users/AnhVuNguyen/Documents/workspace/ComputabilityComplexityAndHeuristics/data/KnapsackRes.RData")

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

## Values
valuesDP1 <- KnapsackRes %>% select(count, DP1.Value)
valuesDP2 <- KnapsackRes %>% select(count, DP2.Value)
valuesGreedy <- KnapsackRes %>% select(count, Greedy.Value)
valuesFPTAS <- KnapsackRes %>% select(count, FPTAS.Value)

ggplot() + 
  geom_point(data = valuesDP1, aes(count, DP1.Value, shape = "DP1", colour = "DP1")) + 
  geom_smooth(data = valuesDP1, aes(count, DP1.Value, colour = "DP1")) + 
  geom_point(data = valuesDP2, aes(count, DP2.Value, shape = "DP2", colour = "DP2")) + 
  geom_smooth(data = valuesDP2, aes(count, DP2.Value, colour = "DP2")) + 
  geom_point(data = valuesGreedy, aes(count, Greedy.Value, shape = "Greedy", colour = "Greedy")) + 
  geom_smooth(data = valuesGreedy, aes(count, Greedy.Value, colour = "Greedy")) + 
  geom_point(data = valuesFPTAS, aes(count, FPTAS.Value, shape = "FPTAS", colour = "FPTAS")) + 
  geom_smooth(data = valuesFPTAS, aes(count, FPTAS.Value, colour = "FPTAS"))


ggplot() + 
  geom_point(data = KnapsackRes, aes(count, DP1.Value, shape = "DP1", colour = "DP1")) + 
  geom_smooth(data = KnapsackRes, aes(count, DP1.Value, colour = "DP1")) + 
  geom_point(data = KnapsackRes, aes(count, DP2.Value, shape = "DP2", colour = "DP2")) + 
  geom_smooth(data = KnapsackRes, aes(count, DP2.Value, colour = "DP2")) + 
  geom_point(data = KnapsackRes, aes(count, Greedy.Value, shape = "Greedy", colour = "Greedy")) + 
  geom_smooth(data = valuesGreedy, aes(count, Greedy.Value, colour = "Greedy")) + 
  geom_point(data = valuesFPTAS, aes(count, FPTAS.Value, shape = "FPTAS", colour = "FPTAS")) + 
  geom_smooth(data = valuesFPTAS, aes(count, FPTAS.Value, colour = "FPTAS"))

#### 3SAT EXPERIMENTS ####

ReductionRes <- na.omit(reduction_res)
ReductionRes$DP1.Running.Time <- as.numeric(removeMS(ReductionRes$DP1.Running.Time))
ReductionRes$DP2.Running.Time <- as.numeric(removeMS(ReductionRes$DP2.Running.Time))
ReductionRes$Greedy.Running.Time <- as.numeric(removeMS(ReductionRes$Greedy.Running.Time))
ReductionRes$FPTAS.Running.Time <- as.numeric(removeMS(ReductionRes$FPTAS.Running.Time))

summary(ReductionRes)

## Save this dataframe for report
save(ReductionRes, file="/Users/AnhVuNguyen/Documents/workspace/ComputabilityComplexityAndHeuristics/data/ReductionRes.RData")

## Statistical Summary
stat.desc(ReductionRes$DP1.Running.Time, basic = F)
stat.desc(ReductionRes$DP2.Running.Time, basic = F)
stat.desc(ReductionRes$Greedy.Running.Time, basic = F)
stat.desc(ReductionRes$FPTAS.Running.Time, basic = F)