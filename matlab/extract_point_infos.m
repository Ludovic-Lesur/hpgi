function [name, distance, altitude, hour] = extract_point_infos(str)
space_idx = 1 ;
for char_idx = 1:1:length(str)
    if str(char_idx) == ' '
        if space_idx == 1
            name = str(1:char_idx-1);
            mem = char_idx + 1;
            space_idx = 2;
        elseif space_idx == 2
            distance = str2double(str(mem:char_idx-1));
            mem = char_idx + 1;
            space_idx = 3;
        elseif space_idx == 3
            altitude = str2double(str(mem:char_idx-1));
            mem = char_idx + 1;
            space_idx = 4;
        end
    end
end
hour = str(mem:length(str));