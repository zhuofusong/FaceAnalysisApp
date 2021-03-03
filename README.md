# FaceAnalysisApp
Android/iOS app for analyzing facial characteristics based on anatomical knowledge, etc

The """android/opencamera""" folder contains a modified version of an early 2021 snapshot of the open source camera app developed by Mark Harman. The original [code repo](https://sourceforge.net/projects/opencamera/) has latest updates and features. The added functionalities include

1. facial landmarks detection
2. facial landmarks analysis
   
The medical analysis applied on the facial picture results from a simple rule-based inference using [NIH](https://www.nlm.nih.gov/research/umls/sourcereleasedocs/current/DDB/index.html)'s [disease database](http://www.diseasesdatabase.com/). All inferred diseases are linked with the original description on these websites. 

Obviously, there are a lot of abnormalities associated with the head of a human body when running searches in a common medical DB. [This page](http://www.diseasesdatabase.com/snomed/118934005), for example, lists all head-related diseases in a hierarchical way. For our simple application, we try to identify the most easily-detected head/skull symptoms using facial landmarks, starting with diseases like [hypertelorism](http://www.diseasesdatabase.com/snomed/22006008), which is defined using the [endocanthion](https://www.merriam-webster.com/dictionary/endocanthion) and exocanthion landmarks. ([more about three‐dimensional landmarks on the human face](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC4832301/) can be found in the link). 

The final app provides a "diagnosis" report using the definition of skull abnormalities and measured facial indices. Additionally, using the localization functionality of the camera app, it can calculate distribution curve of all the indices of a population group in a certain region. Inference about the percentile (with respect to a generated regional distribution curve) of a person in a new picture is made using the population distribution curve. 


## Usage:

### Basic usage

There are two modes, online and offline detection. 

1. Offline detection: After installing the apps in this folder following the instructions in their respective folders, the user can start the facial landmark detection app and capture photos. These photos and generated facial statistics will be saved to a default folder which is modifiable from within the app. Then the user can start the symptom checkers app and import these photos for viewing the disease search results.

2. Online detection: After opening the facial landmark detection app and configuring the diseases to be detected, the user can view in real time the tags put near identified faces. In recording mode, the identified unique faces and the tagging results will be saved in a session log, linked with the recording video file. 

After installing the ```mlkit vision``` app on a device or simulator, choose the ```kotlin``` branch and then ```cameraX``` branch. Accept necessary permissions and when the camera opens, select ```face detection``` mode. Make sure in the app's own settings, ```classification mode``` and ```detection all contours``` are selected. Then you should be able to see facial indices such as canthal index in real time along with other classification metrics (like eyes being opened probability). 

### Application and extension

This app can be used for the surveillance purpose, generating alerts in the situation where diseases are detected. It can be easily integrated with other apps which may implement further actions based on detected profiles. 

## To-do:

### Fix mlkit vision bugs:

- [ ] Auto mirror view and correct left/right mislabeling
- [ ] Reduce app size

### General

- [ ] Use the opencamera frontend since it provides more camera configurations
- [ ] Extend the symptoms list for diseases detection
- [ ] Add algorithms based on video analysis techniques for cross validation of detection results
- [ ] Add functionalities for saving run-time statistics
- [ ] Link facial features with other inferred variables
- [ ] Faces clustering and profiles clustering


## Installation

This repo contains development code. For stable versions of the product, please refer to the releases section.

To compile the code yourself, import the project ```mlkit vision``` into Android Studio and build the project and run on a supported device or simulator.
