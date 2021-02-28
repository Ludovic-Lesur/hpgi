#!/bin/bash
pwd
echo "Start executing Matlab script in $PWD/matlab"
 matlab -nodisplay -nosplash -nodesktop -r "run('$PWD/matlab/hpgi.m'); exit;"
echo "End of script."
