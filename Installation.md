# QuPath[^1]

To install qupath:

1. Go to: https://qupath.github.io/
2. On Windows: download the .zip file (UF computers have some security requirements that make the .msi file not work properly)
3. Extract the file to a location of your choice, preferably **"C:\Users\\user-name\\"** (e.g. "C:\Users\jdoe\")
   1. It will look like: **C:\Users\\user-name\\QuPath-[version]**


# Segment Anything SAM [^2] [^3]

Follow the instruction on: 

* https://github.com/ksugar/qupath-extension-sam
* https://github.com/ksugar/samapi

1. Download the .jar
2. Drag and drop the .jar file in QuPath or Copy the .jar to
   * **"C:\Users\\user-name\\QuPath\\v0.4\\"** folder (e.g. "C:\Users\jdoe\\QuPath\\v0.4\\")


## Usage
1. Activate the conda environment 
```
conda activate samapi
uvicorn samapi.main:app --workers 2
```
2. Open QuPath
3. Go to **Extensions** > **SAM** > **SAM API**

# References

[^1]: QuPath - https://qupath.github.io/ 

[^2]: SAM paper - https://arxiv.org/abs/2304.02643

[^3]: SAM website - https://segment-anything.com/