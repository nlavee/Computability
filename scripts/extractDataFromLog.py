import csv

## Read from log file
f = open('/Users/AnhVuNguyen/Documents/workspace/ComputabilityComplexityAndHeuristics/log/Computability.log', 'r')

## Write to csv file
csvfile = open('/Users/AnhVuNguyen/Documents/workspace/ComputabilityComplexityAndHeuristics/data/res.csv', 'wb')
## Write headers
fieldnames = ['count', 'DP1 Value', 'DP1 Running Time', 'DP2 Value', 'DP2 Running Time', 'Greedy Value', 'Greedy Running Time', 'FPTAS Value', 'FPTAS Running Time']
writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
writer.writeheader()

## Read all lines
lines = f.readlines()
for i in range(0, len(lines)):
        line = lines[i]
        #print line
        if line.startswith( 'Count: ' ):
		# start of a block of log for one instance
			count = line[7:].strip()
			print count
			
			DP1 = lines[i+1]
			DP1Value = DP1[7:].strip()
			print DP1Value
			DP1 = lines[i+2]
			DP1RunningTime = DP1[14:].strip()
			print DP1RunningTime

			DP2 = lines[i+3]
			DP2Value = DP2[18:].strip()
			print DP2Value
			DP2 = lines[i+4]
			DP2RunningTime = DP2[14:].strip()
			print DP2RunningTime

			Greedy = lines[i+5]
			GreedyValue = Greedy[8:].strip()
			print GreedyValue
			Greedy = lines[i+6]
			GreedyRunningTime = Greedy[14:].strip()
			print GreedyRunningTime

			FPTAS = lines[i+7]
			FPTASValue = FPTAS[7:].strip()
			print FPTASValue
			FPTAS = lines[i+8]
			FPTASRunningTime = FPTAS[14:].strip()
			print FPTASRunningTime

			writer.writerow({'count': count, 'DP1 Value': DP1Value, 'DP1 Running Time':DP1RunningTime, 'DP2 Value':DP2Value, 'DP2 Running Time':DP2RunningTime, 'Greedy Value':GreedyValue, 'Greedy Running Time':GreedyRunningTime, 'FPTAS Value':FPTASValue, 'FPTAS Running Time':FPTASRunningTime})
			i = i + 8


