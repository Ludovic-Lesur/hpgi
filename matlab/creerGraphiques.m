close all ;
clc ;
set(0, 'defaulttextinterpreter', 'latex') ;
set(groot, 'defaultAxesTickLabelInterpreter', 'latex') ;
set(groot, 'defaultLegendInterpreter', 'latex') ;

%% OUVERTURE DU FICHIER INDIQUANT LA RANDONNEE COURANTE

randoCourante = fopen('randoCourante.txt', 'rt') ;
nomRando = fgetl(randoCourante) ;

%% OUVERTURE DU FICHIER DE LA RANDONNEE

disp(strcat('Graphiques de la randonnée', {' '}, nomRando)) ;
source = strcat('randos/', nomRando, '_Matlab.txt') ;
data = fopen(source, 'rt') ;
rando = fgetl(data) ;
numEtapes = str2double(fgetl(data)) ;

%% GRAPHE DE CHAQUE extractEtape

dMin = -1 ;
dMax = 25 ;
altMin = -350 ;
altMax = 3499 ;
tailleNom = 8 ;

% Graphiques hebdomadaires de la randonnée
dim = 0 ;
distCumul = 0 ;
dPosCumul = 0 ;
dNegCumul = 0 ;
n_t = cell(1, numEtapes*50) ;
n_etiq = cell(1, numEtapes+1) ;
Orange = [1 0.4 0] ;
Vert = [0 0.7 0] ;
d_t = zeros(1, numEtapes*50) ;
d_etiq = zeros(1, numEtapes+1) ;
a_t = zeros(1, numEtapes*50) ;
a_etiq = zeros(1, numEtapes+1) ;
h_t = cell(1, numEtapes*50) ;
nbGraphes = 1+floor((numEtapes-1)/7) ;
limSemaines = zeros(1,nbGraphes) ;
indice = 1 ;

disp('Generation des graphiques de chaque etape...') ;

% Boucle des étapes
for i=1:1:numEtapes
    
    etape = formater(fgetl(data)) ;
    [numPoints longueur dPos dNeg] = extractEtape(fgetl(data)) ;
  
    n = cell(1, numPoints) ;
    d = zeros(1, numPoints) ;
    a = zeros(1, numPoints) ;
    h = cell(1, numPoints) ;

    % Boucle des points
    for j=1:1:numPoints
        point = fgetl(data) ;
        % Graphes hebdomadaires de la traversée
        [n_t{dim+j} d_t(1,dim+j) a_t(1,dim+j) h_t{dim+j}] = extractPoint(point) ;
        d_t(1,dim+j) = d_t(1,dim+j)+distCumul ;
        n_t{dim+j} = formater(n_t{dim+j}) ;
        if j == 1
            n_etiq{i} = n_t{dim+j} ;
            d_etiq(i) = d_t(dim+j) ;
            a_etiq(i) = a_t(dim+j) ;
        end
        if i == numEtapes && j==numPoints
            n_etiq{i+1} = n_t{dim+j} ;
            d_etiq(i+1) = d_t(dim+j) ;
            a_etiq(i+1) = a_t(dim+j) ;
        end
        % Graphe de l'étape
        [n{j} d(1,j) a(1,j) h{j}] = extractPoint(point) ;
        n{j} = formater(n{j}) ;
    end
    dim = dim + numPoints ;
    if mod(i,7) == 0
        limSemaines(1,indice) = dim ;
        indice = indice+1 ;
    end
    distCumul = distCumul+d(1,numPoints) ;

    % Affichage de l'étape
    screen = get(0, 'ScreenSize') ;
    figure('Position', [100 100 screen(3)-100 screen(4)-100], 'visible', 'off') ;
    plot(d, a, 'k:.') ;
    axis([dMin, dMax, altMin, altMax]) ;
    title(etape, 'interpreter', 'latex') ;
    xlabel('Distance (km)', 'interpreter', 'latex') ;
    ylabel('Altitude (m)', 'interpreter', 'latex') ;
    set(gca, 'FontName', 'Latin Modern Roman') ;

    % Positionnement des étiquettes
    for k=1:1:numPoints
        if a(k) < 2000
            % Coloration des toponymes
            if k == 1 || k==numPoints
                text(d(k), a(k)+50, n{k}, 'Color', Orange, 'FontSize', tailleNom, 'Rotation', 90) ;
            elseif strcmp(n{k}(1:4), 'MIDI') == 1
                text(d(k), a(k)+50, n{k}(5:end), 'Color', Vert, 'FontSize', tailleNom, 'Rotation', 90) ;
            else
                text(d(k), a(k)+50, n{k}, 'FontSize', tailleNom, 'Rotation', 90) ;
            end
            text(d(k), a(k)-50, h{k}, 'Color', 'Blue', 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
        else
            if k == 1 || k==numPoints
                text(d(k), a(k)-50, n{k}, 'Color', Orange, 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
            elseif strcmp(n{k}(1:4), 'MIDI') == 1
                text(d(k), a(k)-50, n{k}(5:end), 'Color', Vert, 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
            else
                text(d(k), a(k)-50, n{k}, 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
            end
            text(d(k), a(k)+50, h{k}, 'Color', 'Blue', 'FontSize', tailleNom, 'Rotation', 90) ;
        end
    end
    
    % Informations de l'étape
    strLongueur = strcat('Longueur $\\ $ = $\\ $ ', num2str(longueur), ' km') ;
    text(dMax-5, 3150, strLongueur) ;
    strDPos = strcat('Denivele + $\\ $ = $\\ $ ', num2str(dPos), ' m') ;
    text(dMax-5, 3000, strDPos) ;
    dPosCumul = dPosCumul + dPos ;
    strDNeg = strcat('Denivele $-$ $\\ $ = $\\ $ ', num2str(dNeg), ' m') ;
    text(dMax-5, 2850, strDNeg) ;
    dNegCumul = dNegCumul + dNeg ;
    
    % Sauvegarde du graphique en PDF
    fig = gcf ;
    set(fig, 'PaperPositionMode', 'auto') ;
    set(fig, 'PaperOrientation', 'landscape') ;
    nomPDF = strcat('C:/Users/Ludovic/Documents/Eclipse/HPGI/output/', rando, '/Etape_', num2str(i), '.pdf') ;
    print(gcf, '-dpdf', nomPDF) ;
    close all ;
end

%% GRAPHIQUES HEBDOMADAIRES DE LA RANDONNEE

n_tr = n_t(1,1:dim) ;
d_tr = d_t(1,1:dim) ;
a_tr = a_t(1,1:dim) ;
h_tr = h_t(1,1:dim) ;

disp('Generation des graphiques hebdomadaires...') ;

% Affichage hedbomadaire de la traversée
for i = 1:1:nbGraphes
    
    screen = get(0, 'ScreenSize') ;
    figure('Position', [100 100 screen(3)-100 screen(4)-100], 'visible', 'off') ;
    if i == 1
        % Première semaine
        if nbGraphes == 1
            plot(d_tr, a_tr, 'k:') ;
        else
            plot(d_tr(1,1:limSemaines(1,1)), a_tr(1,1:limSemaines(1,1)), 'k:') ;
        end
    elseif i == nbGraphes
        % Dernière semaine
        plot(d_tr(1,limSemaines(1,i-1)+1:dim), a_tr(1,limSemaines(1,i-1)+1:dim), 'k:') ;
    else
        plot(d_tr(1,limSemaines(1,i-1)+1:limSemaines(1,i)), a_tr(1,limSemaines(1,i-1)+1:limSemaines(1,i)), 'k:') ;
    end
   
    % Ajustement de l'axe horizontal
    if i == nbGraphes
        distHebdo = d_etiq(end)-d_etiq(7*(i-1)+1) ;
        dMax = d_etiq(end)+(1/22)*distHebdo ;
    else
        distHebdo = d_etiq(7*i+1)-d_etiq(7*(i-1)+1) ;
        dMax = d_etiq(7*i+1)+(1/22)*distHebdo ;
    end
    dMin = d_etiq(7*(i-1)+1)-(1/22)*distHebdo ;
    axis([dMin, dMax, altMin, altMax]) ;
    
    if numEtapes <= 7
        if numEtapes == 1
            titre = strcat('\textbf{', formater(rando), '} $ \quad $ (', num2str(numEtapes), ' etape)') ;
        else
            titre = strcat('\textbf{', formater(rando), '} $ \quad $ (', num2str(numEtapes), ' etapes)') ;
        end
        
    else
        titre = strcat('\textbf{', formater(rando), '} $ \quad $ (', num2str(numEtapes), ' etapes) $ \quad $ \textbf{SEMAINE $ $', num2str(i), '}') ;
    end
    title(titre, 'interpreter', 'latex') ;
    xlabel('Distance (km)', 'interpreter', 'latex') ;
    ylabel('Altitude (m)', 'interpreter', 'latex') ;
    set(gca, 'FontName', 'Latin Modern Roman') ;

    hold on ;
    
    % Positionnement des étiquettes
    if i == nbGraphes
        % Dernière semaine
        plot(d_etiq(1,7*(i-1)+1:length(d_etiq)), a_etiq(1,7*(i-1)+1:length(a_etiq)), 'k.') ;
        for k = 7*(i-1)+1:1:length(d_etiq)
            if a_etiq(k) < 2000
                text(d_etiq(k), a_etiq(k)+50, n_etiq{k}, 'FontSize', tailleNom, 'Rotation', 90) ;
                text(d_etiq(k), a_etiq(k)-50, strcat(num2str(a_etiq(k)), ' m'), 'Color', 'Blue', 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
            else
                text(d_etiq(k), a_etiq(k)-50, n_etiq{k}, 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
                text(d_etiq(k), a_etiq(k)+50, strcat(num2str(a_etiq(k)), ' m'), 'Color', 'Blue', 'FontSize', tailleNom, 'Rotation', 90) ;
            end
        end
    else
        plot(d_etiq(7*(i-1)+1:7*i+1), a_etiq(7*(i-1)+1:7*i+1), 'k.') ;
        for k = 7*(i-1)+1:1:7*i+1
            if a_etiq(k) < 2000
                text(d_etiq(k), a_etiq(k)+50, n_etiq{k}, 'FontSize', tailleNom, 'Rotation', 90) ;
                text(d_etiq(k), a_etiq(k)-50, strcat(num2str(a_etiq(k)), ' m'), 'Color', 'Blue', 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
            else
                text(d_etiq(k), a_etiq(k)-50, n_etiq{k}, 'FontSize', tailleNom, 'Rotation', 90, 'HorizontalAlignment', 'right') ;
                text(d_etiq(k), a_etiq(k)+50, strcat(num2str(a_etiq(k)), ' m'), 'Color', 'Blue', 'FontSize', tailleNom, 'Rotation', 90) ;
            end
        end
    end

    % Informations de la traversée
    strLongueur = strcat('Longueur $\\ $ = $\\ $ ', num2str(distCumul), ' km') ;
    decalage = dMax-(4.5/22)*distHebdo ;
    text(decalage, 3150, strLongueur) ;
    strDPos = strcat('Denivele + $\\ $ = $\\ $ ', num2str(dPosCumul), ' m') ;
    text(decalage, 3000, strDPos) ;
    strDNeg = strcat('Denivele $-$ $\\ $ = $\\ $ ', num2str(dNegCumul), ' m') ;
    text(decalage, 2850, strDNeg) ;

    % Sauvegarde du graphique hebdomadaire en PDF
    fig = gcf ;
    set(fig, 'PaperPositionMode', 'auto') ;
    set(fig, 'PaperOrientation', 'landscape') ;
    nomPDF = strcat('C:/Users/Ludovic/Documents/Eclipse/HPGI/output/', rando, '/Semaine_', num2str(i), '.pdf') ;
    print(gcf, '-dpdf', nomPDF) ;
    clc ;
    close all ;
end

%% FIN DU PROGRAMME

fclose(data) ;
clear all ;
close all ;
clc ;
disp('Programme termine avec succes.') ;
