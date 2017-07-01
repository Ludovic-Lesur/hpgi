function new = formater(str)
new = str ;
for i=1:1:length(str)
    if str(i) == '_'
         new(i) = ' ' ;   
    end
end
end