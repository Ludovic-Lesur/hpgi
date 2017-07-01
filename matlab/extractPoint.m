function [n d a h] = extractPoint(str)
numEspace = 1 ;
for i=1:1:length(str)
    if str(i) == ' '
        if numEspace == 1
            n = str(1:i-1) ;
            mem = i+1 ;
            numEspace = 2 ;
        elseif numEspace == 2
            d = str2double(str(mem:i-1)) ;
            mem = i+1 ;
            numEspace = 3 ;
        elseif numEspace == 3
            a = str2double(str(mem:i-1)) ;
            mem = i+1 ;
            numEspace = 4 ;
        end
    end
end
h = str(mem:length(str)) ;