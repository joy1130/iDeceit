load 'UserData/userIndex.mat';

releaseInterval = 5; %5 min
START = 8*60+00; % 8am
END = 20*60 + 00; % 20hrs
timeSlots = (END-START)/releaseInterval; % 8am to 8pm = 20hrs

superUserID = 999;
load 'UserData/locToIndex_999.mat';
numSuperStates = length(superLocToIndex);
superMC = zeros(numSuperStates, numSuperStates, timeSlots+5);

%for i = 1:length(tmobileUsers)
for i = 1:1
    userID = tmobileUsers(i);
    fileName = sprintf('UserData/locToIndex_%d.mat',userID);
    locToIndex = importdata(fileName);
    numStates = length(locToIndex);
    MC = zeros(numStates, numStates, timeSlots+1);
    
    j = 0;
    currTime = 0;
    nextTime = START;
    
    currAreaID = 0;
    prevAreaID = 0;
    lastAreaID = 0;
    
    sameDay = 1;
    prevDate = '';
    currDate = '';
    userLocs = s(userID).locs;
    tmp = find(userLocs(:,2) == 0);
    userLocs(tmp, :, :) = [];
    
    [numRows numCols] = size(userLocs);
    while( j < numRows )
     % Skip Entries till 8am
        while((currTime < nextTime) && (j < numRows))
            prevAreaID = currAreaID;           
            prevDate = currDate;
            j = j + 1;
            currTime = str2num(datestr(userLocs(j,1),'HH'))*60 ...
            + str2num(datestr(userLocs(j,1),'MM'));
            currAreaID = fix(userLocs(j,2));
            currDate = datestr(userLocs(j,1),'dd');
        end
        
        if((lastAreaID ~= 0) && (currTime < END))
            % Update MC between last and prev area IDs for currTime
            timeIndex = fix((currTime-START)/releaseInterval)+1;
            fromIndex = find(locToIndex == fix(lastAreaID/1000));
            toIndex = find(locToIndex == fix(prevAreaID/1000));
            MC(fromIndex, toIndex, timeIndex) = MC(fromIndex, toIndex, timeIndex) + 1;
            
            fromIndex = find(superLocToIndex == fix(lastAreaID/1000));
            toIndex = find(superLocToIndex == fix(prevAreaID/1000));
            superMC(fromIndex, toIndex, timeIndex) = superMC(fromIndex, toIndex, timeIndex) + 1;
        end
        lastAreaID = prevAreaID;
        nextTime = nextTime + releaseInterval;
        
        if(nextTime > END)
            sameDay = 1;
            prevDate = currDate;
            while((sameDay == 1) && (j < numRows))
                j = j + 1;
                currDate = datestr(userLocs(j,1),'dd');
                currTime = str2num(datestr(userLocs(j,1),'HH'))*60 ...
            + str2num(datestr(userLocs(j,1),'MM'));
                currAreaID = fix(userLocs(j,2));
                if(strcmp(currDate,prevDate)==0)
                    sameDay = 0;
                    prevDate = currDate;
                else
                    prevDate = currDate;
                end
            end
            nextTime = START;
            sameDay = 1;
            lastAreaID = 0;
        end
        
    end
    for t = 1:timeSlots
        for p = 1:numStates
            if(sum(MC(p,:,t)) ~= 0)
                MC(p,:,t) = MC(p,:,t)/sum(MC(p,:,t));
            end
        end
    end
    mcFileName = sprintf('Graphs/MC_%d.dot',userID);
    fd = fopen(mcFileName,'w');
    fwrite(fd, sprintf('digraph M {\n'));
    labelStr = '';
    graphStr = '';
    
    for t = 1:timeSlots+1
        for p = 1:numStates
            rowSum = sum(MC(p,:,t));
            if(rowSum ~= 0 )
                str = sprintf('%d:%d',p, t);
                labelStr = strcat(labelStr, sprintf('\n %d [label=\"%s\" pos=\"%d,%d!\"] \n',t*1000+locToIndex(p),str,t,p));
            end
        end
        if( t >= 2 )
            for q = 1:numStates
                if(MC(p,q,t) ~= 0)
                    graphStr = strcat(graphStr, sprintf('\n %d -> {%d} [label=%3.3f]',(t-1)*1000+locToIndex(p),t*1000+locToIndex(q),MC(p,q,t)));
                end
            end
        end
    end
    fwrite(fd, labelStr);
    fwrite(fd, graphStr);
    fwrite(fd, sprintf('\n }'));
    fclose(fd);
end
for t = 1:timeSlots+1
    for p = 1:numSuperStates
        if(sum(superMC(p,:,t)) ~= 0)
            superMC(p,:,t) = superMC(p,:,t)/sum(superMC(p,:,t));
        end
    end
end