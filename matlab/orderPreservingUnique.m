function uniqueArr = orderPreservingUnique(inputVector)
uniqueArr = zeros(length(unique(inputVector)), 1);
j = 0;
for i = 1:length(inputVector)
    if(isempty(find(uniqueArr == inputVector(i), 1)))
        j = j + 1;
        uniqueArr(j) = inputVector(i);
    end
end
end

