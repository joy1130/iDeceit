%load ../RealityMining/realitymining.mat;
% Execute each cell independently one after the other

%% Compute the list of users who use T-mobile as their providers
numUsers = length(s);
tmobileUsers = [];
k = 0;
mkdir ('./', 'UserData');
numMonths = 2; %Users with atleast one month of data

for i = 1:numUsers
    providerName = s(i).my_provider;
    timePeriod = s(i).timeon;
    if(~isempty(providerName) && ~isempty(timePeriod))
        if((strcmp(providerName{1}, 'T-mobile') == 1) && (timePeriod > numMonths*30))
            k = k + 1;
            tmobileUsers(k) = i;
        end
    end
end
save('UserData/userIndex.mat', 'tmobileUsers');
%%
% numDays = zeros(length(tmobileUsers),1);
% factor = 1000;
% maxAreaID = -1;
% unique1000 = [];
% k = 0;
% numSlots = (24*60)/5;
% for i = 1:length(tmobileUsers)
%     userID = tmobileUsers(i);
%     sprintf('Processing data for userID = %d\n',userID)
%     prevDate = '';
%     prevTime = 0;
%     for j = 1:length(s(userID).locs)
%         currDate = datestr(s(userID).locs(j,1),'dd');
%         currTime = s(userID).locs(j,1);
%         if(prevTime == 0)
%             prevTime = currTime;
%         end
%         if(strcmp(currDate, prevDate) == 0)
%             %sprintf('Date = %s, currDate = %s, prevDate = %s\n',datestr(s(userID).locs(j,1),'mm-dd-yyyy'), currDate, prevDate)
%             prevDate = currDate;
%             numDays(i) = numDays(i) + 1;
%             prevTime = currTime;
%         end
%         sprintf('CT = %s, PT = %s, Int = %s, %s, %s\n',datestr(currTime, 'HH:MM:SS'), datestr(prevTime, 'HH:MM:SS'), datestr(currTime - prevTime,'HH:MM:SS'))
%         
%         areaID = fix(s(userID).locs(j,2));
%        
%         if(areaID > maxAreaID)
%             maxAreaID = areaID;
%         end
%         if(isempty(find(unique1000 == fix(areaID/1000), 1)))
%             k = k + 1;
%             unique1000(k) = fix(areaID/1000);
%         end
%         prevTime = currTime;
%     end
% end

%% Create Location to Index Map for each user and write to file
factor = 1000;
superLocToIndex = [];
l = 0;
load 'UserData/userIndex.mat';
sFileName = sprintf('UserData/locToIndex_%d.mat',999);
for i = 1:length(tmobileUsers)
    userID = tmobileUsers(i);
    locToIndex = [];
    k = 0;
    sprintf('Processing data for userID = %d\n',userID)
    fileName = sprintf('UserData/locToIndex_%d.mat',userID);
    for j = 1:length(s(userID).locs)
        areaID = fix(s(userID).locs(j,2));
        if(isempty(find(locToIndex == fix(areaID/1000), 1)))
            k = k + 1;
            locToIndex(k) = fix(areaID/1000);
        end
        if(isempty(find(superLocToIndex == fix(areaID/1000), 1)))
            l = l + 1;
            superLocToIndex(l) = fix(areaID/1000);
        end
    end
    save(fileName, 'locToIndex');
end
save(sFileName, 'superLocToIndex');