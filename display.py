from matplotlib import pyplot as plt
import numpy as np
import pandas as pd

data = pd.read_csv("./best_feature_data/smallBack.csv")
f1 = 0
f2 = 2
f3 = 3

labels = data.iloc[:, 0]
features = data.iloc[:, 1:]
feature_names = list(features.columns.values)
row, col = features.shape
count_x = 0
count_y = 0

fig = plt.figure()
ax = fig.add_subplot(projection='3d')

color= ['blue' if l == 1 else 'red' for l in labels]

#plt.title("Small Dataset, Backwards Elimination: Feature " + feature_names[f1] + " vs Feature " + feature_names[f2])
#plt.title("Small Dataset, Backwards Elimination: Feature " + feature_names[f1] + " vs Feature " + feature_names[f2] + " vs Feature " + feature_names[f3])
plt.title("Small Dataset, Backwards Elimination: Features " + feature_names[f1] + ", " + feature_names[f2] + " vs Feature " + feature_names[f3])

x_values = np.array(data[feature_names[f1]])
y_values = np.array(data[feature_names[f2]])
z_values = np.array(data[feature_names[f3]])
#plt.scatter(x_values, y_values, color = color, alpha = 0.25)
ax.scatter(x_values, y_values, z_values, color = color, alpha = 0.25)
#plt.xlabel("Feature " + feature_names[f1])
#plt.ylabel("Feature " + feature_names[f2])
ax.set_xlabel("Feature " + feature_names[f1])
ax.set_ylabel("Feature " + feature_names[f2])
ax.set_zlabel("Feature " + feature_names[f3])

#plt.tight_layout()
plt.show()