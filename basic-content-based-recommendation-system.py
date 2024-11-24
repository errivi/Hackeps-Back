# 1. Import libraries

import pandas as pd
import os
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
from stop_words import get_stop_words

#   pandas: for reading csv file
#   TfidfVectorizer (Freqüència del terme - Freqüència inversa del document): for text data tokenization
#   linear_kernel: for similarity calculation


# 2. Import data

df = pd.read_csv(os.getcwd() + '/tramits.csv')

df['Titol'] = df['Titol'].fillna('')


# 3. Tokenize text data

# Create a list of stopwords in Catalan language
CatalanStopWords = get_stop_words('catalan')
CatalanStopWords.append('des')
CatalanStopWords.append('que')

# Create a TfidfVectorizer and Remove stopwords
tfidf = TfidfVectorizer(stop_words=CatalanStopWords)


# Fit and transform the data to a tfidf matrix
tfidf_matrix = tfidf.fit_transform(df['Titol'])   

# Print the shape of the tfidf_matrix
# tfidf_matrix.shape


# 4. Calculate the similarity

# Compute the cosine similarity between each movie description
cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)

# 5. Create a function
indices = pd.Series(df.index, index=df['Titol']).drop_duplicates()


def get_recommendations(title, cosine_sim=cosine_sim, num_recommend = 10):
    idx = indices[title] 
    
    # Get the pairwsie similarity scores of all movies with that movie
    sim_scores = list(enumerate(cosine_sim[idx]))
    
    # Sort the movies based on the similarity scores
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    
    # Get the scores of the 10 most similar movies
    top_similar = sim_scores[1:num_recommend+1]
    
    # Get the movie indices
    movie_indices = [i[0] for i in top_similar]
    
    # Return the top 10 most similar movies
    return df['Titol'].iloc[movie_indices]



# Examples

print('TEST1')
print(get_recommendations('Canvi de titular', num_recommend = 4))
print('TEST2')
print(get_recommendations("Canvi de titular", num_recommend = 20))
print('TEST3')
print(get_recommendations("Dades obertes - col·labora en l'Open Data", num_recommend = 20))
