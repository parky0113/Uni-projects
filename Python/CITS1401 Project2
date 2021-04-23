#File: Project1.py
#Made by Sean(sunghyun) Park
#Student Number: 22209962
#Version 1.0.7

"""
This program analyses warc file from Common Crawl.
It needs warc file name, positive words file name and negative words file name.

After execution, it will return 4 items.
First item will contain how many positive and negative words are used in Australian website
and average word count per website. Also, it will return ratio of words.

Second item will tell whether Australians are positive or negative towards own government.
As like first item, it will give how many good or bad sentences for government,
ratio of good and bad and average count per website.

Third item will tell that how Australians are patriotic.
It will compare among few major English speaking countries(Canada, UK).
It will give a percentage which is calculated by words indicating their country
divided by total of words for every website.

Fourth item will give top five websites most used among Australiansand its counts.
"""

def read_file(fname):

    # read file and return text or list depends on type of file

    """
    In this case there are two possible types.
    """

    try:
        if '.warc' in fname:
            warc_file_handler = open(fname, 'rb')
            text = warc_file_handler.read().decode('ascii','ignore')
        elif '.txt' in fname:
            read_text = open(fname, 'r')
            text = read_text.read()
    except OSError:
        return None
    except TypeError:
        return None
    return text


def txt_list(txt):
    """
    if words txt is needed list transformation.
    """
    txt = txt.split("\n")
    del txt[-1:]
    return txt


def block_finder(txt):

    # this function will cut off all other unnecessary infomation.

    """
    This will find block that are only interested.
    In this case, we are looking for raw html with WARC-Type: response,
    Content-Type: text/html and only raw html.
    """

    url_html_dic = {}
    while "WARC/1.0\r\nWARC-Type: response" in txt:
        _, _, tail = txt.partition("WARC/1.0\r\nWARC-Type: response")
        info, sep2, tail = tail.partition("WARC/1.0")
        txt = sep2 + tail

        _, _, tail = info.partition("WARC-Target-URI: ")
        url, sep2, tail = tail.partition("\r\n")

        _, _, http = info.partition("\r\n\r\n")

        _, _, tail = http.partition("Content-Type: ")
        content, sep2, start = tail.partition("\r\n")
        start = sep2 + start

        _, _, tail = start.partition("\r\n\r\n")
        html, _, tail = tail.partition("WARC/1.0")

        if "text/html" in content:
            url_html_dic[url] = html
    return url_html_dic


def domain_finder(dic, country):

    # this function will find url with given TLDs.

    """
    In this case ca,uk,au will be distinct its domain.
    """

    new_dic = {}
    for url in dic.keys():
        if f'.{country}/' in url:
            new_dic[url] = dic.get(url)
        elif f'.{country}:' in url:
            new_dic[url] = dic.get(url)
    return new_dic


def html_cleaner(dic):

    # this function will clean 'dirty' raw html for easier analyzation.

    """
    In this case, all punctuations are going to be ignored.
    Also, html tags and javascripts are going to be deleted.
    """

    punt = [33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,
    58,59,60,61,62,63,64,91,92,93,94,95,96,123,124,125,126]
    for url in dic.keys():
        dirty = dic.get(url)
        while dirty.find("<script") != -1:
            head, _, tail = dirty.partition("<script")
            _, _, tail = tail.partition(">")

            _, _, tail = tail.partition("</script")
            _, _, tail = tail.partition(">")
            dirty = head + tail

        while dirty.find("<") != -1:
            head, _, tail = dirty.partition("<")
            _, _, tail = tail.partition(">")
            dirty = head + tail

        while dirty.find("\n") != -1:
            dirty = dirty.replace("\n"," ")

        while dirty.find("\r") != -1:
            dirty = dirty.replace("\r"," ")

        while dirty.find("\t") != -1:
            dirty = dirty.replace("\t"," ")

        for elem in punt:
            dirty = dirty.replace(chr(elem),"")

        dirty = dirty.strip()
        clean = dirty.lower()

        spt = clean.split(" ")
        spt = list(filter(None, spt))
        dic[url] = spt

    return dic


def words_count(pos_lst,neg_lst, dic):

    # this function will count how many pos and neg words are in raw html.

    """
    It will count every single word in html its existency in words list.
    """

    both_words = []
    pos_count = 0
    neg_count = 0
    pos_set = set(pos_lst)
    neg_set = set(neg_lst)

    for word in pos_lst:
        if word in neg_lst:
            both_words.append(word)

    for url in dic.keys():
        spt = dic.get(url)
        for word in spt:
            if word in both_words:
                pos_count += 1
                neg_count += 1
            elif word in pos_set:
                pos_count += 1
            elif word in neg_set:
                neg_count += 1
    return pos_count,neg_count


def sentence_cleaner(dic):

    # this function will clean 'dirty' raw html for easier analyzation.

    """
    This function alike with a function html_cleaner,
    but it won't remove sentence-ending punctuation to be able to aplit by sentence.
    """

    punt = [34,35,36,37,38,39,40,41,42,43,44,45,47,
    58,59,60,61,62,64,91,92,93,94,95,96,123,124,125,126]
    for url in dic.keys():
        dirty = dic.get(url)
        while dirty.find("<script") != -1:
            head, _, tail = dirty.partition("<script")
            _, _, tail = tail.partition(">")

            _, _, tail = tail.partition("</script")
            _, _, tail = tail.partition(">")
            dirty = head + tail

        while dirty.find("<") != -1:
            head, _, tail = dirty.partition("<")
            _, _, tail = tail.partition(">")
            dirty = head + tail

        while dirty.find("\n") != -1:
            dirty = dirty.replace("\n"," ")

        while dirty.find("\r") != -1:
            dirty = dirty.replace("\r"," ")

        while dirty.find("\t") != -1:
            dirty = dirty.replace("\t"," ")

        dirty = dirty.strip()
        dirty = dirty.replace('?','.')
        dirty = dirty.replace('!','.')
        for elem in punt:
            dirty = dirty.replace(chr(elem),"")
        clean = dirty.lower()

        dic[url] = clean
    return dic


def gov_sentiment(dic,pos_words,neg_words):

    # this function will count sentence which is about government.

    """
    If there are two negative words in one sentence, it will be counted as positive
    because of double negative.
    """

    gov_dic = {}
    neg_sentence = 0
    pos_sentence = 0
    both_words = []
    pos_set = pos_words
    neg_set = neg_words

    for word in pos_words:
        if word in neg_words:
            both_words.append(word)

    for i in dic.keys():
        gov_dic[i] = []
        html = str(dic[i])
        dic[i] = html.split(".")
        for j in range(len(dic[i])):
            test = str(dic[i][j])
            test = test.split(" ")
            test = list(filter(None, test))
            test = set(test)
            count = 0
            if 'government' in test:
                count += 1
            if count != 0:
                gov_dic[i].append(dic[i][j])

    for key in gov_dic.keys():
        for num in range(len(gov_dic[key])):
            sen = gov_dic[key][num]
            sen = sen.split(" ")
            sen = list(filter(None, sen))
            pos_count = 0
            neg_count = 0
            for word in sen:
                if word in both_words:
                    pos_count = pos_count + 1
                    neg_count = neg_count + 1
                elif word in pos_set:
                    pos_count = pos_count + 1
                elif word in neg_set:
                    neg_count = neg_count + 1
            if pos_count == 0 and neg_count == 0:
                continue
            elif pos_count > 0 and neg_count > 0:
                continue
            elif neg_count == 1:
                neg_sentence += 1
            elif neg_count >= 3:
                neg_sentence += 1
            elif neg_count == 2:
                pos_sentence += 1
            elif pos_count >= 1:
                pos_sentence += 1
    return (pos_sentence,neg_sentence)


def pat_country(dic, country):

    # this function will count words indicating their country in their website.

    """
    Occurance of words indicating country will tell how much people are patriotic.
    It will be compared between Australia, UK and Canda.
    """

    if country == "au":
        country_count = 0
        word_count = 0
        for url in dic.keys():
            html = dic.get(url)
            for word in html:
                if word == 'australia':
                    country_count += 1
                    word_count += 1
                elif word != "":
                    word_count += 1
    if country == "ca":
        country_count = 0
        word_count = 0
        for url in dic.keys():
            html = dic.get(url)
            for word in html:
                if word == 'canada':
                    country_count += 1
                    word_count += 1
                elif word != "":
                    word_count += 1
    if country == 'uk':
        country_count = 0
        word_count = 0
        for url in dic.keys():
            html = dic.get(url)
            for i in range(len(html)):
                if html[i] == 'uk':
                    country_count += 1
                    word_count += 1
                elif html[i] == 'united':
                    if html[i+1] == 'kingdom':
                        country_count += 1
                        word_count += 1
                elif html[i] == 'great':
                    if html[i+1] == 'britain':
                        country_count += 1
                        word_count += 1
                elif html[i] != "":
                    word_count += 1
    return country_count, word_count


def href_finder(dic):

    # this function will find website domain in html.

    """
    This function is similar to previous 'cleaner' function but acutally,
    it will detect domain in '<a ...>' tags.
    """

    url_dic = {}
    for url in dic.keys():
        html = dic[url]
        while html.find('<a') != -1:
            _, _, href = html.partition('<a')
            web,_,html = href.partition('>')
            if "href" in web:
                _,_,tail = web.partition('href')

                if tail.startswith("='"):
                    if tail.startswith("='https://"):
                        _,_,tail = tail.partition("='https://")
                        web,aus,_ = tail.partition('.au')
                    elif tail.startswith("='http://"):
                        _,_,tail = tail.partition("='http://")
                        web,aus,_ = tail.partition('.au')
                elif tail.startswith('="'):
                    if tail.startswith('="https://'):
                        _,_,tail = tail.partition('="https://')
                        web,aus,_ = tail.partition('.au')
                    elif tail.startswith('="http://'):
                        _,_,tail = tail.partition('="http://')
                        web,aus,_ = tail.partition('.au')
                web = web + aus
                if web != "":
                    url_dic[web] = url_dic.get(web,0) + 1
    def loop_list(tup):
        return tup[1]
    url_list = list(url_dic.items())
    url_list.sort(key=loop_list,reverse=True)
    top_five = url_list[0:5]
    return top_five


def main(WARC_fname, positive_words_fname, negative_words_fname):

    """
    This main function is divided in to 9 section.
    These will generate analyzations which were mentioned at the begining.
    """

    # for the general
    try:
        if read_file(positive_words_fname) is None:
            return [],[],[],[]
        elif read_file(negative_words_fname) is None:
            return [],[],[],[]
        elif read_file(WARC_fname) is None:
            return [],[],[],[]
        else:
            pos_words = txt_list(read_file(positive_words_fname))
            neg_words = txt_list(read_file(negative_words_fname))
    except NameError:
        return [],[],[],[]

    interested_block = block_finder(read_file(WARC_fname))

    au_block = domain_finder(interested_block , 'au')

    clean_au = html_cleaner(au_block)

    # for the Insight 1
    pos_count, neg_count = words_count(pos_words, neg_words, clean_au)

    # answer for Insight 1
    if neg_count == 0 and len(clean_au) == 0:
        first_insight = [pos_count, neg_count,None,None,None]
    elif neg_count == 0:
        first_insight = [pos_count, neg_count,None,
        round(pos_count/(len(clean_au)),4),round(neg_count/(len(clean_au)),4)]
    elif len(clean_au) == 0:
        first_insight = [pos_count, neg_count,round(pos_count/neg_count,4),None,None]
    else:
        first_insight = [pos_count, neg_count,round(pos_count/neg_count,4),
        round(pos_count/(len(clean_au)),4),round(neg_count/(len(clean_au)),4)]

    # for the Insight 2

    au_block = domain_finder(interested_block , 'au')

    clean_sen = sentence_cleaner(au_block)

    pos_sen, neg_sen = gov_sentiment(clean_sen,pos_words,neg_words)

    # answer for Insight 2
    if neg_sen == 0 and len(au_block) == 0:
        second_insight = [pos_sen, neg_sen,None,None,None]
    elif neg_sen == 0:
        second_insight = [pos_count, neg_count,None,
        round(pos_sen/(len(au_block)),4), round(neg_sen/(len(au_block)),4)]
    elif len(au_block) == 0:
        second_insight = [pos_sen, neg_sen, round(pos_sen/neg_sen,4),None,None]
    else:
        second_insight = [pos_sen, neg_sen, round(pos_sen/neg_sen,4),
        round(pos_sen/(len(au_block)),4), round(neg_sen/(len(au_block)),4)]

    # for the Insight 3
    ca_block = domain_finder(interested_block, 'ca')
    uk_block = domain_finder(interested_block, 'uk')
    cleaned_uk, cleaned_ca = html_cleaner(uk_block), html_cleaner(ca_block)
    occ_au, agg_au = pat_country(clean_au,'au')
    occ_ca, agg_ca = pat_country(cleaned_ca,'ca')
    occ_uk, agg_uk = pat_country(cleaned_uk,'uk')

    # answer for Insight 3
    if agg_au == 0:
        auc = None
    else:
        auc = round((occ_au/agg_au)*100,4)

    if agg_ca == 0:
        cac = None
    else:
        cac = round((occ_ca/agg_ca)*100,4)

    if agg_uk == 0:
        ukc = None
    else:
        ukc = round((occ_uk/agg_uk)*100,4)

    third_insight = [auc,cac,ukc]

    # for the Insight 4
    au_block = domain_finder(interested_block, 'au')

    # answer for Insight 4
    forth_insight = href_finder(au_block)

    return first_insight, second_insight, third_insight, forth_insight
print(main('warc_sample_file.warc', 'positive_words.txt', 'negative_words.txt'))
