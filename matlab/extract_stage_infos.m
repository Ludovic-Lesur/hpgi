function [number_of_points, stage_length, altitude_delta_p, altitude_delta_n] = extract_stage_infos(str)
space_idx = 1;
for char_idx = 1:1:length(str)
    if str(char_idx) == ' '
        if space_idx == 1
            number_of_points = str2double(str(1:char_idx-1));
            mem = char_idx + 1;
            space_idx = 2;
        elseif space_idx == 2
            stage_length = str2double(str(mem:char_idx-1));
            mem = char_idx + 1;
            space_idx = 3;
        elseif space_idx == 3
            altitude_delta_p = str2double(str(mem:char_idx-1));
            mem = char_idx + 1;
            space_idx = 4;
        end
    end
end
altitude_delta_n = str2double(str(mem:length(str)));
end