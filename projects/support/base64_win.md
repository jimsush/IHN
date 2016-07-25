certutil -encode inputFileName encodedOutputFileName

certutil -decode encodedInputFileName decodedOutputFileName


remove the first line and last line,
cat b64.txt | base64 -di >  orgin.zip (linux)

base64 orgin.zip > b64.txt (linux)
