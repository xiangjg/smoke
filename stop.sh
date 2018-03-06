ps -ef | grep smoke | grep -v grep | awk '{print $2}' | xargs kill -9
