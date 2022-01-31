# File name: A3_22209962.py
# Made by Sunghyun(Sean) Park
# Student NO.22209962
# It is made for Assignment3 in CITS2401

"""
This program is made for data analysing and visualising
It will go through functions and make sure the data is suitable for visualisation
It has 11 functions in total.
"""



import csv
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
from collections import Counter

def load_metrics(filename):
    """
    this function will read csv file
    with delimiter and quotechar
    then data stored into np array
    """
    ofile = open(filename)
    data = csv.reader(ofile, delimiter=",", quotechar='"')
    data = np.array(list(data), dtype = object)
    ofile.close()
    return data[:,(0,1,7,8,9,10,11,12,13)]

def unstructured_to_structured(data, indexes):
    """
    this function will trim first row
    and change ndarray to tuple then
    change dtype of indexes column to '<U30'
    others to 'float'
    """
    data = data[1:,:]
    colist = [('created_at','float'), ('tweet_ID','float'),('valence_intensity','float'),
    ('anger_intensity','float'), ('fear_intensity','float'),('sadness_intensity','float'),
    ('joy_intensity','float'),('sentiment_category','float'), ('emotion_category','float'),]
    for i in indexes:
        colist[i] = (colist[i][0], '<U30')
    data = [tuple(i) for i in data.tolist()]
    data = np.array(data, dtype=colist)
    return data

def converting_timestamps(array):
    """
    this function will change timestamp in data
    into better format for analysis
    this file has only Jan and Feb but added
    monthdic for furthur research
    """
    monthdic = {'Jan' : '01', 'Feb' : '02', 'Mar' : '03', 'Apr' : '04',
                'May' : '05', 'Jun' : '06', 'Jul' : '07', 'Aug' : '08',
                'Sep' : '09', 'Oct' : '10', 'Nov' : '11', 'Dec' : '12'}
    for i in range(len(array)):
        old = array[i].split(" ")
        array[i] = f"{old[5]}-{monthdic[old[1]]}-{old[2]} {old[3]}"
    return array

def replace_nan(data):
    """
    this function will get a average of each intesity columns
    and replace nan to ave
    """
    intensity = ['valence_intensity', 'anger_intensity', 'fear_intensity',
                 'sadness_intensity', 'joy_intensity']
    for val in intensity:
        ave = (np.nansum(data[:][val]))/(np.count_nonzero(~np.isnan(data[:][val])))
        data[:][val] = np.nan_to_num(data[:][val], nan=ave)
    return data

def boxplot_data(data, output_name = 'output'):
    """
    this function will draw a graph
    followed by given instructions
    """
    intensity = ['valence_intensity', 'anger_intensity', 'fear_intensity',
                 'sadness_intensity', 'joy_intensity']
    plotdata = []
    for val in intensity:
        plotdata.append(data[:][val])
    plt.figure(figsize=(10,7))
    plt.grid(True, axis = 'y')
    plt.xlabel('Sentiment')
    plt.ylabel('Values')
    plt.title('Distribution of Sentiment')
    plt.savefig(output_name)
    box = plt.boxplot(plotdata, labels = ['Valence', 'Anger', 'Fear', 'Sadness','Joy'], 
    medianprops = dict(linestyle='-', linewidth=1, color='black'), patch_artist = True, )
    colors = ['green', 'red', 'purple', 'blue', 'yellow']
    for patch, color in zip(box['boxes'], colors):
        patch.set_facecolor(color)
    plt.savefig(output_name)

def number_of_outliers(sentiment, lower, upper):
    """
    this function will return count of outlier given upper & lower quantile
    """
    return sentiment[np.where((sentiment <= np.quantile(sentiment, lower*0.01)) |
                       (sentiment >= np.quantile(sentiment, upper*0.01)))].size

def convert_to_df(data):
    """
    this function will return pandas dataframe which was
    changed from ndarray with their column names
    """
    datf = pd.DataFrame(data, columns = data.dtype.names)
    return datf

def load_tweets(filename):
    """
    this function will read tsv file and put data into pandas DF
    """
    datf = pd.read_csv(filename, sep='\t')
    return datf

def merge_dataframes(df_metrics, df_tweets):
    """
    this function will join to pandas df
    step1. drop all nan vals
    step2. match same column name
    step3. change dtype to int
    step4. assign index and join on common
    """
    df_metrics = df_metrics.dropna()
    df_tweets = df_tweets.dropna()
    df_tweets = df_tweets.rename(columns={df_tweets.columns[0]: 'tweet_ID'})
    df_metrics['tweet_ID'] = pd.to_numeric(df_metrics['tweet_ID'], downcast = 'integer')
    df_tweets = df_tweets.astype({'tweet_ID': 'int64'})
    df_metrics = df_metrics.set_index('tweet_ID')
    df_merged = df_tweets.join(df_metrics, on = 'tweet_ID', how = 'inner')
    return df_merged

def plot_timeperiod(df_merged, from_date, to_date, output_name = 'output'):
    """
    this function will draw line graphs
    x = datetime
    y = intensities
    range fromdate and todate
    and save png as output name
    """
    df_merged['created_at'] = pd.to_datetime(df_merged['created_at'])
    df_merged = df_merged.sort_values(by = ['created_at'])
    plt.figure(figsize=(15, 8))
    drange = (df_merged['created_at'] > from_date) & (df_merged['created_at'] < to_date)
    plotdata = df_merged.loc[drange]
    intensity = ['valence_intensity', 'anger_intensity', 'fear_intensity',
                 'sadness_intensity', 'joy_intensity']
    colours = ['green', 'red', 'purple', 'blue', 'yellow']
    for i in range(len(intensity)):
        plt.plot(plotdata['created_at'], plotdata[:][intensity[i]], label = intensity[i]
                 , color = colours[i])
    plt.legend()
    plt.xticks(rotation = 30, ha = 'right')
    plt.xlabel('created_at')
    plt.savefig(output_name)

def plot_frequency(word_frequency, num, output_name = 'output'):
    """
    this function will draw line graphs
    x = datetime
    y = intensities
    range fromdate and todate
    and save png as output name.
    Stupidly did not utilise provided skeleton code.
    Didn't know it was there, but learnt a lot from it.
    especially, cmap... took me hours
    """
    plt.figure(figsize=(15, 10))
    colours = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
    word = []
    freq = []
    rgb = []
    for color in colours:
        rgb.append(mcolors.to_rgb(color))
    cmap = mcolors.LinearSegmentedColormap.from_list('mycmap', rgb)
    col = [cmap(1.*i/num) for i in range(num)]
    for i in range(num):
        word.append(word_frequency[i][0])
        freq.append(word_frequency[i][1])
    plt.barh(range(num), freq, align = 'center', color = col )
    plt.yticks(range(num), word)
    plt.gca().invert_yaxis()
    plt.xlabel('Frequency')
    plt.title(f'Word Frequency: Top {num}')
    plt.savefig(output_name)

