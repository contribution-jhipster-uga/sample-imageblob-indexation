#from imageai.Detection import ObjectDetection
import os
import sys
import numpy as np

if len(sys.argv) != 2:
    raise Exception('Invalid number of arguments. 1 argument is required.')


execution_path = os.getcwd()
im_name = str(sys.argv[1])
nn_name = 'resnet50_coco_best_v2.0.1.h5'
threshold = 75


detector = ObjectDetection()
detector.setModelTypeAsRetinaNet()
detector.setModelPath( os.path.join(execution_path , nn_name))
detector.loadModel()
detections = detector.detectObjectsFromImage(input_image=os.path.join(execution_path , im_name), output_image_path=os.path.join(execution_path , "output.jpg") , minimum_percentage_probability=threshold)

for eachObject in detections:
    # print(eachObject["name"] , " : ", eachObject["percentage_probability"], " : ", eachObject["box_points"] )
    print(eachObject["name"])
