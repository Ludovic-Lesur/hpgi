function [num l dp dn] = extractEtape(str)
numEspace = 1 ;
for i=1:1:length(str)
    if str(i) == ' '
        if numEspace == 1
            num = str2double(str(1:i-1)) ;
            mem = i+1 ;
            numEspace = 2 ;
        elseif numEspace == 2
            l = str2double(str(mem:i-1)) ;
            mem = i+1 ;
            numEspace = 3 ;
        elseif numEspace == 3
            dp = str2double(str(mem:i-1)) ;
            mem = i+1 ;
            numEspace = 4 ;
        end
    end
end
dn = str2double(str(mem:length(str))) ;
end