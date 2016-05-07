import csv

## Read from log file
f = open('/Users/AnhVuNguyen/Documents/workspace/ComputabilityComplexityAndHeuristics/log/Computability.log', 'r')

## Write to csv file
csvfile = open('/Users/AnhVuNguyen/Documents/workspace/ComputabilityComplexityAndHeuristics/data/reduction_res.csv', 'wb')

## Write headers
fieldnames = ['count', 'DP1 Running Time', 'DP2 Running Time', 'Greedy Running Time', 'FPTAS Running Time']
writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
writer.writeheader()

## Read all lines
lines = f.readlines()
for i in range(0, len(lines)):
        line = lines[i]
        #print line
        if line.startswith( 'Count: ' ):
        	# start of a block of log for one instance
			count = int(line[7:].strip())
			count = count + 1 # go from base-0 to base-1
			print count
			
			DP1 = lines[i+1]
			DP1RunningTime = DP1[3:].strip()
			print DP1RunningTime

			DP2 = lines[i+2]
			DP2RunningTime = DP2[3:].strip()
			print DP2RunningTime

			Greedy = lines[i+3]
			GreedyRunningTime = Greedy[3:].strip()
			print GreedyRunningTime

			FPTAS = lines[i+4]
			FPTASRunningTime = FPTAS[3:].strip()
			print FPTASRunningTime

			writer.writerow({'count': str(count), 'DP1 Running Time':DP1RunningTime, 'DP2 Running Time':DP2RunningTime, 'Greedy Running Time':GreedyRunningTime, 'FPTAS Running Time':FPTASRunningTime})
			i = i + 4