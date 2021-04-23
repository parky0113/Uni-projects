# File name: A2_22209962.py
# Made by Sunghyun(Sean) Park
# Student NO.22209962
# It is made for Assignment2 in CITS2401

#This program is made for analysing tweets.
#It will go through functions and clean data(sentences) for easier analyzing.
#It has 14 functions in total and each of them has brief explanation of what is does

#It is built to use same variable name for each functions for readability
#when variables have same meaning.

def proper_capitalization(sentence):
    """ this function will return changed sentence into lowercase """
    sentence = sentence.lower()
    return sentence

def tokenization(sentence):
    """
    this function will split the sentence to words in list by whitespace
    then return the list, if sentence has no words, return empty list
    """
    words_list = sentence.split(" ")
    if sentence == "":
        return []
    return words_list

def stop_word_removal(sentence, stop_words):
    """
    this function will remove stop_words in sentence
    it takes parameters, sentence and stop words that user wanna remove
    then return cleaned setence
    """
    stop_list = tokenization(stop_words)
    words_list = tokenization(sentence)
    new_sentence = ""
    for word in words_list:
        if word not in stop_list:
            new_sentence += word + " "
    return new_sentence[:-1]

def remove_punc(sentence, punctuation):
    """
    this function takes sentence and punctuation as parameters
    and remove punctuations in the sentence and return it
    """
    words_list = tokenization(sentence)
    for i in range(len(words_list)):
        words_list[i] = words_list[i].rstrip(punctuation)
        words_list[i] = words_list[i].lstrip(punctuation)
    return " ".join(words_list)

def remove_duplicate_words(sentence):
    """ this will delete duplicates in the sentences and return it """
    words_list = tokenization(sentence)
    words_set = sorted(set(words_list))
    return " ".join(words_set)

def cleaning_noise(sentence):
    """
    this function will clean unnecessary data in the senctence.
    the cleaning rules are
    1. "&amp" to "&"
    2. "\n" to " "
    3. every even number of @(metion) will be used for analysis
    4. word cotaining "#" and "http" will be ignored.
    then return cleaned sentence
    """
    sentence = sentence.replace("&amp", "&")
    sentence = sentence.replace("\n", " ")
    words_list = tokenization(sentence)
    new_list = []
    nth = 1
    for word in words_list:
        if "@" in word:
            if nth % 2 == 0:
                new_list.append(word)
            nth += 1
        elif not ("#" in word or "http" in word):
            new_list.append(word)
    return " ".join(new_list)


def construct_ngrams(sentence, n):
    """
    this function return list of lists, each list has n many word in it
    used nested loops for restricting n many words for one list
    """
    words_list = tokenization(sentence)
    new_list = []
    if n <= 0:
        return []
    if len(words_list) < n:
        return []
    if len(words_list) >= n:
        for i in range(len(words_list) - n + 1):
            inner_list = []
            for j in range(n):
                inner_list.append(words_list[i + j])
            new_list.append(inner_list)
    return new_list



# from here next 3 functions are made to help function pos
# helper functions are 'condition_check', 'removing_plurals' and 'removing_past_tense'

def condition_check(word):
    ''' this function will check if there is any exceptions in word
    then if there is return True if not return False '''
    return any([word.endswith("'s"), word.endswith("s'"), word.endswith("s"),
        word.endswith('ed'), word.endswith('er'), word.endswith('ly'), word.endswith("ing")])

def removing_plurals(word):
    """
    this function will follow all rules of removing plurals
    it will return changed word and boolean Ture if word is done pos
    """
    vowel_list = ['a', 'e', 'i', 'o', 'u', 'y', 'A', 'E', 'I', 'O', 'U', 'Y']
    no_vowel = True
    for letter in word:
        if letter in vowel_list:
            no_vowel = False
    if word == "s":
        return '', True
    if word.endswith("ies"):
        if len(word[:-2]) > 2:
            return word[:-2], False
        return word[:-1], False
    if word.endswith("sses"):
        return word[:-2], True
    if any([word[-2] in vowel_list, no_vowel, (word.endswith("us") or word.endswith("ss"))]):
        return word, True
    return word[:-1], False

def removing_past_tense(word):
    """
    this function will remove past tense.
    """
    if word.endswith('ied'):
        if len(word[:-2]) > 2:
            return word[:-2]
        return word[:-1]
    return word[:-2]

def pos(sentence):
    '''
    This function will change a word into root form
    There are many exceptions, and conditions
    '''
    words_list = tokenization(sentence)
    stemmed_list = []
    for word in words_list:
        if word.endswith("\n"):
            word = word[:-1]
        while condition_check(word):
            if word.endswith("'s") or word.endswith("s'"):
                word = (word[:-2])
            elif word.endswith("s"):
                word, exception = removing_plurals(word)
                if exception:
                    break
            elif word.endswith('ed'):
                word = removing_past_tense(word)
            elif word.endswith('er'):
                word = (word[:-2])
            elif word.endswith('ing'):
                if len(word[:-3]) >= 3:
                    word = (word[:-3])
                else:
                    break
            elif word.endswith('ly'):
                word = (word[:-2])
        stemmed_list.append(word)
    return " ".join(stemmed_list)

def load_data(filename):
    """
    this function will open file and read the text
    then return the text
    """
    open_text = open(filename, "r", encoding='utf-8')
    text = open_text.readlines()
    return text

def word_ranking(corpus, n):
    """
    this function will count occurances of word in sentences in list and rank them
    then return nth top ranking by alphabetical order
    """
    freq_dict = {}
    for sentence in corpus:
        words_list = sentence.split(" ")
        for word in words_list:
            if word not in freq_dict:
                freq_dict[word] = 1
            else:
                freq_dict[word] = freq_dict.get(word) + 1
    freq_dict = sorted(freq_dict.items(), key=lambda x: x[1], reverse=True)
    nth_ranking = freq_dict[:n]
    nth_ranking.sort()
    return nth_ranking

def tweet_analysis():
    """
    this file will process all functions for tweet analysis
    and return list of sentences and write a out file
    """
    filename = input("Enter the name of the file to read: ")
    outfile = input("Enter the name of the file to write: ")
    stopwords = input("Enter your stopwords: ")
    punctuations = input("Enter your punctuations to remove: ")
    lines_list = load_data(filename)
    outlist = []
    for line in lines_list:
        line = stop_word_removal(proper_capitalization(line), stopwords)
        line = pos(cleaning_noise(remove_duplicate_words(remove_punc(line, punctuations))))
        outlist.append(line)
    writefile = open(outfile, "w", encoding='UTF8')
    out_text = '\n'.join(outlist)
    writefile.write(out_text)
    return outlist
