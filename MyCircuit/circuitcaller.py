import os


class Caller:
    def Run(self):
        cmd = "java -cp lib/commons-beanutils-1.8.0.jar:lib/commons-collections-3.2.1.jar:lib/commons-lang-2.4.jar:lib/commons-logging-1.1.1.jar:lib/ezmorph-1.0.6.jar:lib/json-lib-2.2.3-jdk15.jar:. Construct_Circuit circuit_json_data.json"
        f = os.popen(cmd)
        result = f.read()
        return result