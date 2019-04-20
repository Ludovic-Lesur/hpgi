%% PROGRAM INIT.

close all;
clc;

%% USE LATEX.

set(0, 'defaulttextinterpreter', 'latex');
set(groot, 'defaultAxesTickLabelInterpreter', 'latex');
set(groot, 'defaultLegendInterpreter', 'latex');

%% MACROS

% Output folder.
output_folder = 'C:/Users/Ludovic/Documents/Eclipse/HPGI/output/';
% X axis limits.
stage_length_max = 25;
% Y axis limits
altitude_min = -350;
altitude_max = 3499;
% Text formatting.
text_size = 8;
text_color_orange = [1 0.4 0];
text_color_green = [0 0.7 0];
% Maximum number of points per stages.
maximum_number_of_points_per_stage = 50;

%% GET CURRENT HIKE.

current_hike = fopen('hike.txt', 'rt');
hike_name = fgetl(current_hike);

%% OPEN HIKE FILE.

disp(strcat(hike_name, {' '}, 'hike graphs'));
hike_file_name = strcat('randos/', hike_name, '_Matlab.txt');
hike_data = fopen(hike_file_name, 'rt');
hike = fgetl(hike_data);
number_of_stages = str2double(fgetl(hike_data));

%% STAGES GRAPHS.

% Data buffers.
name_buf = cell(1, (number_of_stages * maximum_number_of_points_per_stage));
distance_buf = zeros(1, (number_of_stages * maximum_number_of_points_per_stage));
altitude_buf = zeros(1, (number_of_stages * maximum_number_of_points_per_stage));
hour_buf = cell(1, (number_of_stages * maximum_number_of_points_per_stage));

% Prepare hike buffers
hike_name_buf = cell(1, (number_of_stages + 1));
hike_distance_buf = zeros(1, (number_of_stages + 1));
hike_altitude_buf = zeros(1, (number_of_stages + 1));
% Hike stats.
hike_distance = 0;
hike_altitude_delta_p = 0;
hike_altitude_delta_n = 0;

% Weekly buffers.
week_name_buf = cell(1, 8);
week_distance_buf = zeros(1, 8);
week_altitude_buf = zeros(1, 8);
% Weekly stats.
weekly_distance = 0;
weekly_altitude_delta_p = 0;
weekly_altitude_delta_n = 0;

% Global variables.
point_count = 0;
stage_count = 0;
previous_cumulated_length = 0;
week_count = 0;
stage_start_idx = 1;
week_start_idx = 1;

% Stages loop.
for stage_idx = 1:1:number_of_stages
    
    % Get stage informations.
    stage_count = stage_count + 1;
    stage = remove_underscores(fgetl(hike_data));
    [stage_number_of_points, stage_length, stage_altitude_delta_p, stage_altitude_delta_n] = extract_stage_infos(fgetl(hike_data)) ;
    
    % Update hike stats.
    hike_distance = hike_distance + stage_length;
    hike_altitude_delta_p = hike_altitude_delta_p + stage_altitude_delta_p;
    hike_altitude_delta_n = hike_altitude_delta_n + stage_altitude_delta_n;
    
    % Update weekly stats.
    weekly_distance = weekly_distance + stage_length;
    weekly_altitude_delta_p = weekly_altitude_delta_p + stage_altitude_delta_p;
    weekly_altitude_delta_n = weekly_altitude_delta_n + stage_altitude_delta_n;

    % Retrieve all points of current stage.
    for point_idx = 1:1:stage_number_of_points
        point = fgetl(hike_data);
        point_count = point_count + 1;
        [name_buf{1, point_count}, distance_buf(1, point_count), altitude_buf(1, point_count), hour_buf{1, point_count}] = extract_point_infos(point);  
        name_buf{1, point_count} = remove_underscores(name_buf{1, point_count});
        if point_idx == 1
            % Save first stage point of the week for weekly graph.
            if stage_count == 1
                week_start_idx = point_count;
            end
            % Save first hike point for the hike graph.
            if stage_idx == 1
                hike_name_buf{1, 1} = name_buf{1, point_count};
                hike_distance_buf(1) = distance_buf(point_count);
                hike_altitude_buf(1) = altitude_buf(point_count);
            end
        end
    end

    % Build current stage graph.
    screen = get(0, 'ScreenSize');
    figure('Position', [100 100 screen(3)-100 screen(4)-100], 'visible', 'off');
    plot(distance_buf(stage_start_idx:point_count), altitude_buf(stage_start_idx:point_count), 'k:.');
    x_low_limit = -0.04*stage_length_max;
    x_high_limit = 1.04*stage_length_max;
    axis([x_low_limit, x_high_limit, altitude_min, altitude_max]);
    title(stage, 'interpreter', 'latex');
    xlabel('Distance (km)', 'interpreter', 'latex');
    ylabel('Altitude (m)', 'interpreter', 'latex');
    set(gca, 'FontName', 'Latin Modern Roman');
    for point_idx = stage_start_idx:1:point_count
        % Place tags.
        if altitude_buf(point_idx) < 1500
            % Place point name.
            if point_idx == stage_start_idx || point_idx == point_count
                % Colour = orange for departure and arrival points.
                if strcmp(name_buf{point_idx}(1:4), 'MIDI') == 1
                    text(distance_buf(point_idx), altitude_buf(point_idx)+50, name_buf{point_idx}(5:end), 'Color', text_color_orange, 'FontSize', text_size, 'Rotation', 90);
                else
                    text(distance_buf(point_idx), altitude_buf(point_idx)+50, name_buf{point_idx}, 'Color', text_color_orange, 'FontSize', text_size, 'Rotation', 90);
                end
            elseif strcmp(name_buf{point_idx}(1:4), 'MIDI') == 1
                % Colour = green for lunch point.
                text(distance_buf(point_idx), altitude_buf(point_idx)+50, name_buf{point_idx}(5:end), 'Color', text_color_green, 'FontSize', text_size, 'Rotation', 90);
            else
                % Default colour for other points.
                text(distance_buf(point_idx), altitude_buf(point_idx)+50, name_buf{point_idx}, 'FontSize', text_size, 'Rotation', 90);
            end
            % Place hour.
            text(distance_buf(point_idx), altitude_buf(point_idx)-50, hour_buf{point_idx}, 'Color', 'Blue', 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
        else
            % Place point name.
            if point_idx == stage_start_idx || point_idx == point_count
                % Colour = orange for departure and arrival points.
                if strcmp(name_buf{point_idx}(1:4), 'MIDI') == 1
                    text(distance_buf(point_idx), altitude_buf(point_idx)-50, name_buf{point_idx}(5:end), 'Color', text_color_orange, 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
                else
                    text(distance_buf(point_idx), altitude_buf(point_idx)-50, name_buf{point_idx}, 'Color', text_color_orange, 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
                end
            elseif strcmp(name_buf{point_idx}(1:4), 'MIDI') == 1
                % Colour = green for lunch point.
                text(distance_buf(point_idx), altitude_buf(point_idx)-50, name_buf{point_idx}(5:end), 'Color', text_color_green, 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
            else
                % Default colour for other points.
                text(distance_buf(point_idx), altitude_buf(point_idx)-50, name_buf{point_idx}, 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
            end
            % Place hour.
            text(distance_buf(point_idx), altitude_buf(point_idx)+50, hour_buf{point_idx}, 'Color', 'Blue', 'FontSize', text_size, 'Rotation', 90);
        end
    end
    % Print stage stats.
    length_string = strcat('Longueur $\\ $ = $\\ $ ', num2str(stage_length), ' km');
    text(0.8*x_high_limit, 0.90*altitude_max, length_string);
    altitude_delta_p_string = strcat('Denivele + $\\ $ = $\\ $ ', num2str(stage_altitude_delta_p), ' m');
    text(0.8*x_high_limit, 0.85*altitude_max, altitude_delta_p_string);
    altitude_delta_n_string = strcat('Denivele $-$ $\\ $ = $\\ $ ', num2str(stage_altitude_delta_n), ' m');
    text(0.8*x_high_limit, 0.80*altitude_max, altitude_delta_n_string);
    % Save graph in PDF.
    fig = gcf;
    set(fig, 'PaperPositionMode', 'auto');
    set(fig, 'PaperOrientation', 'landscape');
    pdf_name = strcat(output_folder, hike, '/Etape_', num2str(stage_idx), '.pdf');
    print(gcf, '-dpdf', pdf_name);
    close all;
    
    % Convert stage distance to cumulated distance.
    for point_idx = stage_start_idx:1:point_count
        distance_buf(point_idx) = distance_buf(point_idx) + previous_cumulated_length;
    end
    previous_cumulated_length = distance_buf(point_count);
    
    % Update next stage start index.
    stage_start_idx = point_count + 1;
    
    % Save first point for weekly graph.
    if stage_count == 1
        week_name_buf{1, 1} = name_buf{1, week_start_idx};
        week_distance_buf(1) = distance_buf(week_start_idx);
        week_altitude_buf(1) = altitude_buf(week_start_idx);
    end
    
    % Save last stage point for weekly graph. 
    week_name_buf{1, stage_count+1} = name_buf{1, point_count};
    week_distance_buf(stage_count+1) = distance_buf(point_count);
    week_altitude_buf(stage_count+1) = altitude_buf(point_count);
    
    % Save last stage point for hike graph.
    hike_name_buf{1, stage_idx+1} = name_buf{1, point_count};
    hike_distance_buf(stage_idx+1) = distance_buf(point_count);
    hike_altitude_buf(stage_idx+1) = altitude_buf(point_count);

    % Build weekly graph.
    if stage_count == 7 || stage_idx == number_of_stages
        % Update counter.
        week_count = week_count + 1;
        % Create figure.
        screen = get(0, 'ScreenSize');
        figure('Position', [100 100 screen(3)-100 screen(4)-100], 'visible', 'off');
        plot(distance_buf(week_start_idx:point_count), altitude_buf(week_start_idx:point_count), 'k:');
        hold on;
        plot(week_distance_buf(1:stage_count+1), week_altitude_buf(1:stage_count+1), 'k.');
        x_low_limit = distance_buf(week_start_idx) - 0.04*weekly_distance;
        x_high_limit = distance_buf(point_count) + 0.04*weekly_distance;
        axis([x_low_limit, x_high_limit, altitude_min, altitude_max]);
        week_title = strcat('\textbf{', remove_underscores(hike), '} $ \quad $ (', num2str(number_of_stages), ' etapes) $ \quad $ \textbf{SEMAINE $ $', num2str(week_count), '}');
        title(week_title, 'interpreter', 'latex');
        xlabel('Distance (km)', 'interpreter', 'latex');
        ylabel('Altitude (m)', 'interpreter', 'latex');
        set(gca, 'FontName', 'Latin Modern Roman');
        % Place tags.
        for point_idx = 1:1:(stage_count+1)
            if week_altitude_buf(point_idx) < 1500
                % Place point name.
                if strcmp(week_name_buf{point_idx}(1:4), 'MIDI') == 1
                    text(week_distance_buf(point_idx), week_altitude_buf(point_idx)+50, week_name_buf{point_idx}(5:end), 'FontSize', text_size, 'Rotation', 90);
                else
                    text(week_distance_buf(point_idx), week_altitude_buf(point_idx)+50, week_name_buf{point_idx}, 'FontSize', text_size, 'Rotation', 90);
                end
                % Place altitude.
                text(week_distance_buf(point_idx), week_altitude_buf(point_idx)-50, strcat(num2str(week_altitude_buf(point_idx)), ' m'), 'Color', 'Blue', 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
            else
                % Place point name.
                if strcmp(week_name_buf{point_idx}(1:4), 'MIDI') == 1
                    text(week_distance_buf(point_idx), week_altitude_buf(point_idx)-50, week_name_buf{point_idx}(5:end), 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
                else
                    text(week_distance_buf(point_idx), week_altitude_buf(point_idx)-50, week_name_buf{point_idx}, 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
                end
                % Place altitude.
                text(week_distance_buf(point_idx), week_altitude_buf(point_idx)+50, strcat(num2str(week_altitude_buf(point_idx)), ' m'), 'Color', 'Blue', 'FontSize', text_size, 'Rotation', 90);
            end
        end
        % Print weekly stats.
        length_string = strcat('Longueur $\\ $ = $\\ $ ', num2str(weekly_distance), ' km');
        text((x_low_limit+0.8*(x_high_limit-x_low_limit)), 0.90*altitude_max, length_string);
        altitude_delta_p_string = strcat('Denivele + $\\ $ = $\\ $ ', num2str(weekly_altitude_delta_p), ' m');
        text((x_low_limit+0.8*(x_high_limit-x_low_limit)), 0.85*altitude_max, altitude_delta_p_string);
        altitude_delta_n_string = strcat('Denivele $-$ $\\ $ = $\\ $ ', num2str(weekly_altitude_delta_n), ' m');
        text((x_low_limit+0.8*(x_high_limit-x_low_limit)), 0.80*altitude_max, altitude_delta_n_string);
        % Reset stats for next week.
        weekly_distance = 0;
        weekly_altitude_delta_p = 0;
        weekly_altitude_delta_n = 0;
        stage_count = 0;
        % Save graph in PDF.
        fig = gcf;
        set(fig, 'PaperPositionMode', 'auto');
        set(fig, 'PaperOrientation', 'landscape');
        pdf_name = strcat(output_folder, hike, '/Semaine_', num2str(week_count), '.pdf');
        print(gcf, '-dpdf', pdf_name);
        close all;
    end
end

% Whole hike graph.
screen = get(0, 'ScreenSize');
figure('Position', [100 100 screen(3)-100 screen(4)-100], 'visible', 'off');
plot(distance_buf(1:point_count), altitude_buf(1:point_count), 'k:');
hold on;
plot(hike_distance_buf, hike_altitude_buf, 'k.');
x_low_limit = -0.04*hike_distance;
x_high_limit = 1.04*hike_distance;
axis([x_low_limit, x_high_limit, altitude_min, altitude_max]);
hike_title = strcat('\textbf{', remove_underscores(hike), '} $ \quad $ (', num2str(number_of_stages), ' etapes) $ \quad $ \textbf{RANDO COMPLETE}');
title(hike_title, 'interpreter', 'latex');
xlabel('Distance (km)', 'interpreter', 'latex');
ylabel('Altitude (m)', 'interpreter', 'latex');
set(gca, 'FontName', 'Latin Modern Roman');
% Place tags (only departure and arrival).
for point_idx = [1 (number_of_stages+1)]
    if hike_altitude_buf(point_idx) < 1500
        % Place point name.
        if strcmp(hike_name_buf{point_idx}(1:4), 'MIDI') == 1
            text(hike_distance_buf(point_idx), hike_altitude_buf(point_idx)+50, hike_name_buf{point_idx}(5:end), 'FontSize', text_size, 'Rotation', 90);
        else
            text(hike_distance_buf(point_idx), hike_altitude_buf(point_idx)+50, hike_name_buf{point_idx}, 'FontSize', text_size, 'Rotation', 90);
        end
        % Place altitude.
        text(hike_distance_buf(point_idx), hike_altitude_buf(point_idx)-50, strcat(num2str(hike_altitude_buf(point_idx)), ' m'), 'Color', 'Blue', 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
    else
        % Place point name.
        if strcmp(hike_name_buf{point_idx}(1:4), 'MIDI') == 1
            text(hike_distance_buf(point_idx), hike_altitude_buf(point_idx)-50, hike_name_buf{point_idx}(5:end), 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
        else
            text(hike_distance_buf(point_idx), hike_altitude_buf(point_idx)-50, hike_name_buf{point_idx}, 'FontSize', text_size, 'Rotation', 90, 'HorizontalAlignment', 'right');
        end
        % Place altitude.
        text(hike_distance_buf(point_idx), hike_altitude_buf(point_idx)+50, strcat(num2str(hike_altitude_buf(point_idx)), ' m'), 'Color', 'Blue', 'FontSize', text_size, 'Rotation', 90);
    end
end
% Print hike stats.
length_string = strcat('Longueur $\\ $ = $\\ $ ', num2str(hike_distance), ' km');
text(0.8*x_high_limit, 0.90*altitude_max, length_string);
altitude_delta_p_string = strcat('Denivele + $\\ $ = $\\ $ ', num2str(hike_altitude_delta_p), ' m');
text(0.8*x_high_limit, 0.85*altitude_max, altitude_delta_p_string);
altitude_delta_n_string = strcat('Denivele $-$ $\\ $ = $\\ $ ', num2str(hike_altitude_delta_n), ' m');
text(0.8*x_high_limit, 0.80*altitude_max, altitude_delta_n_string);
% Save graph in PDF.
fig = gcf;
set(fig, 'PaperPositionMode', 'auto');
set(fig, 'PaperOrientation', 'landscape');
pdf_name = strcat(output_folder, hike, '/RandoComplete.pdf');
print(gcf, '-dpdf', pdf_name);
close all;

%% PROGRAM END.

fclose(hike_data);
close all;
clc;
disp('Program successfully completed.');
