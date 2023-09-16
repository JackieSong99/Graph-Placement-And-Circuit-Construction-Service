import os
import sys

if __name__ == '__main__':
    filename = sys.argv[1]
    cmd = "java -cp lib/commons-beanutils-1.8.0.jar:lib/commons-collections-3.2.1.jar:lib/commons-lang-2.4.jar:lib/commons-logging-1.1.1.jar:lib/ezmorph-1.0.6.jar:lib/json-lib-2.2.3-jdk15.jar:. Run json_file/"  + filename
    f = os.popen(cmd)
    print(f.read())
