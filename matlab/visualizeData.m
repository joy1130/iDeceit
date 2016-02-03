% Effort to visualize the data from RM
load 'UserData/userIndex.mat';
uniqueIDArr = [];
sprintf('Processing data to compute list of unique towers')
for i = 1:length(tmobileUsers)
    userID = tmobileUsers(i);    
%     towerIDArr = fix(s(userID).locs(:,2));
%     areaIDArr = fix(s(userID).locs(:,2));
%      
%     cellIDArr =abs(s(i).locs(:,2) - areaID);
    towerIDArr = s(userID).locs(:,2);
    tmp = find(towerIDArr == 0);
    towerIDArr(tmp, :) = [];
    uniqueIDArr = unique([uniqueIDArr;towerIDArr]);
end

% histArr = zeros(length(uniqueIDArr),1);
% 
% for i = 1:length(tmobileUsers)
%     userID = tmobileUsers(i);
%     towerIDArr = fix(s(userID).locs(:,2));
%     tmp = find(towerIDArr == 0);
%     towerIDArr(tmp, :, :) = [];
%     histArr = histArr + histc(towerIDArr, uniqueIDArr);
% end
% 
% bar(histArr);

% Perform Spectral Clustering

%Step 1: Compute Weighted Adjacency Matrix
sprintf('Computing Weighted Adjacency Matrix')
adjMat = zeros(length(uniqueIDArr), length(uniqueIDArr));
for i = 1:length(tmobileUsers)
    userID = tmobileUsers(i);
    towerIDArr = fix(s(userID).locs(:,2));
    tmp = find(towerIDArr == 0);
    towerIDArr(tmp, :) = [];
    numSamples = length(towerIDArr);
    
    lastState = towerIDArr(1);
    for j = 2:numSamples
        currState = towerIDArr(j);
        from = find(uniqueIDArr == lastState);
        to = find(uniqueIDArr == currState);
        adjMat(from, to) = adjMat(from, to) + 1;
        lastState = currState;
    end
    
end

% Step 2: Compute the diagonal matrix
sprintf('Computing Diagonal Matrix')
oneVec = ones(length(uniqueIDArr), 1);
D = diag(adjMat*oneVec);

%Step 3: Compute the Laplacian Matrix
sprintf('Compute the Laplacian Matrix')
L = D - adjMat;

%Step 4: Compute the EigenVectors and EigenValues
sprintf('Compute the EigenVectors and EigenValues')
[V La] = eigs(L, 20, 'sm');

sprintf('Perform Clustering of the towers')
[indexToClusterMap C] = kmeans(V, 20, 'emptyaction','drop','start','cluster');
sprintf('Number of clusters = %d\n',length(unique(indexToClusterMap)))

% Rewrite the data in terms of clusters for the users
sprintf('Rewriting loc data in terms of clusters')
% mkdir ('./', 'ClusterData');
save('ClusterData/indexToClusterMap.mat','indexToClusterMap');
save('ClusterData/uniqueIDArr.mat','uniqueIDArr');
save('ClusterData/userIndex.mat','tmobileUsers');

uniqueLocsArr = zeros(length(tmobileUsers),1);
for i = 1:length(tmobileUsers)
    userID = tmobileUsers(i);
    fileName = sprintf('ClusterData/locData_%d.mat',userID);
    locData = [s(userID).locs(:,1) fix(s(userID).locs(:,2))];
    tmp = find(locData(:,2) == 0);
    locData(tmp, :) = [];
    for j = 1:length(locData)
        locData(j,2) = indexToClusterMap(find(uniqueIDArr==locData(j,2)));
    end
    uniqueLocsArr(i) = length(unique(locData(:,2)));
    save(fileName, 'locData');
end