import random

import pandas as pd
import plotly.express as px

"""
Flips heads or tails with small delay
"""


def flipHeadOrTails():
    value = random.randint(0, 100)
    if (value < 49):
        return "Heads"
    elif (49 < value < 100):
        return "Tails"
    else:
        return "Nothing"


def generateFlips(range=range(0, 1000)):
    list = []
    for _ in range:
        list.append(flipHeadOrTails())

    return list


flips = generateFlips()

trueValues = [val for val in flips if val == "Heads"]
falseValues = [val for val in flips if val == "Tails"]
nothingValues = [val for val in flips if val == "Nothing"]
# Create a DataFrame with counts of Heads and Tails
df = pd.DataFrame({
    "Outcome": ["Heads", "Tails", "Nothing"],
    "Count": [len(trueValues), len(falseValues), len(nothingValues)]
})

# Create bar chart
fig = px.bar(df,
             x="Outcome", y="Count",
             color="Outcome",
             # color_discrete_map={'Heads': 'red', "Tails": "green"},
             title="Heads vs Tails Count")

fig.show()
