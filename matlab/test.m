numUsers = length(s);
temp = [];
numMonths = 2;
for i = 1:numUsers
    startDate = getDateNum(s(i).my_startdate);
    if(startDate ~= 0)
        endDate = getDateNum(s(i).my_enddate);
        if((endDate ~= 0) && (endDate - startDate > numMonths*30) && (~isempty(s(i).cellnames)))
            a = cellfun(@isempty, s(i).cellnames);
            a(a(:,2)==1,:) = 1;
            a = s(i).cellnames(~a);
            a = reshape(a, length(a)/2,[]);
            temp = [temp; unique(a(:,2))];
        end
    end
end

disp(length(unique(lower(temp))))