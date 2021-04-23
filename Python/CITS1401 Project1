#File: Project1.py
#Made by Sean(sunghyun) Park

"""
This program calculate statistical value and return
such as min, max, avg, sd from given data file and arguments.

for example,
main(csvfile,year,type)

csvfile - name of the file in string format

year - a year or years(list) which you want to analyze

type - either 'stats' or 'corr'
('stats' gives user a sing year statistic and 'corr' gives user a correlation between two years.)

possible outputs will be:

stats - 4 lists contains min, max, average, sd value for each month of the year

corr - 4 values which is correlation between two years in min, max, average, sd.
"""

year_dict = {"stat":[[] for i in range(12)],"corr":[[] for i in range(12)]}

def min_list_maker(dict2 = 0):
    """
    This function make a list of minimum value
    for each month with rain fall data. (>0.0)
    """
    min_list = []
    if dict2 == 0:
        for month in range(len(year_dict["stat"])):
            if not[x for x in year_dict["stat"][month] if x !=0]:
                min_list.append(0.0)
            else:
                min_list.append(min(x for x in year_dict["stat"][month] if x !=0))
    elif dict2 != 0:
        for month in range(len(year_dict["corr"])):
            if not[x for x in year_dict["corr"][month] if x !=0]:
                min_list.append(0.0)
            else:
                min_list.append(min(x for x in year_dict["corr"][month] if x !=0))
    return min_list


def max_list_maker(dict2 = 0):
    """
    This function make a list of maximum value
    for each month with rain fall data.
    """
    max_list = []
    if dict2 == 0:
        for month in range(len(year_dict["stat"])):
            if year_dict["stat"][month] == []:
                max_list.append(0.0)
            else:
                max_list.append(max(year_dict["stat"][month]))
    elif dict2 != 0:
        for month in range(len(year_dict["corr"])):
            if year_dict["corr"][month] == []:
                max_list.append(0.0)
            else:
                max_list.append(max(year_dict["corr"][month]))
    return max_list


def avg_list_maker(dict2 = 0):
    """
    This function make a list of average value
    for each month with rain fall data.
    """
    avg_list = []
    if dict2 == 0:
        for month in range(len(year_dict["stat"])):
            if year_dict["stat"][month] == []:
                avg_list.append(0.0)
            else:
                sum_rainfall = sum(year_dict["stat"][month])
                len_month_year = len(year_dict["stat"][month])
                avg_num = round((sum_rainfall / len_month_year),4)
                avg_list.append(avg_num)
    elif dict2 != 0:
        for month in range(len(year_dict["corr"])):
            if year_dict["corr"][month] == []:
                avg_list.append(0.0)
            else:
                sum_rainfall = sum(year_dict["corr"][month])
                len_month_year = len(year_dict["corr"][month])
                avg_num = round((sum_rainfall / len_month_year),4)
                avg_list.append(avg_num)
    return avg_list


def std_list_maker(dict2 = 0):
    """
    This function make a list of standard deviation
    for each month with rain fall data.
    """
    std_list = []
    if dict2 == 0:
        for month in range(len(year_dict["stat"])):
            std_avg_list = []
            if year_dict["stat"][month] == []:
                std_list.append(0.0)
            else:
                for datum_rain in year_dict["stat"][month]:
                    sum_rainfall = sum(year_dict["stat"][month])
                    len_month_year = len(year_dict["stat"][month])
                    var_rainfall = (datum_rain - (sum_rainfall / len_month_year))
                    std_avg_list.append(var_rainfall**2)
                std_num = round(((sum(std_avg_list)/len(std_avg_list))**0.5),4)
                std_list.append(std_num)
    elif dict2 != 0:
        for month in range(len(year_dict["corr"])):
            std_avg_list = []
            if year_dict["corr"][month] == []:
                std_list.append(0.0)
            else:
                for datum_rain in year_dict["corr"][month]:
                    sum_rainfall = sum(year_dict["corr"][month])
                    len_month_year = len(year_dict["corr"][month])
                    var_rainfall = (datum_rain - (sum_rainfall / len_month_year))
                    std_avg_list.append(var_rainfall**2)
                std_num = round(((sum(std_avg_list)/len(std_avg_list))**0.5),4)
                std_list.append(std_num)
    return std_list


def correlation_coe(lst1, lst2):
    """
    This function makes a list of correlation
    between two data set of rainfall.
    """
    sd1,sd2,var1,var2,var3 = [],[],[],[],[]
    if sum(lst1) == 0 :
        r_value = 0.0
    elif sum(lst2) == 0:
        r_value = 0.0
    else:
        for i in lst1:
            var1.append(i - (sum(lst1) / len(lst1)))
        for i in lst2:
            var2.append(i - (sum(lst2) / len(lst2)))
        for var1_num,var2_num in zip(var1,var2):
            var3.append(var1_num * var2_num)
        for i in range(len(lst1)):
            sd1.append((var1[i])**2)
            sd2.append((var2[i])**2)
        std1, std2 = (sum(sd1))**0.5,(sum(sd2))**0.5
        r_value = sum(var3) / (std1 * std2)
        r_value = round(r_value,4)
    return r_value


def main(csvfile, year, type):
    """
    This main function will distinguish and give
    statistic values that user wants.
    """
    try:
        file_to_read = open(csvfile,"r")
        file_list = file_to_read.readlines()
        del file_list[0]

        for i in file_list:
            i = i[:-1].split(",")
            del i[0]
            if i[3] == "" or float(i[3]) < 0:
                i[3] = 0
            if type == "stats":
                if str(year) == i[0]:
                    year_dict["stat"][int(i[1])-1].append(float(i[3]))
            elif type == "corr":
                if i[0] == str(year[0]):
                    year_dict["stat"][int(i[1])-1].append(float(i[3]))
                elif i[0] == str(year[1]):
                    year_dict["corr"][int(i[1])-1].append(float(i[3]))

        if type == "stats":
            s_min = min_list_maker()
            s_max = max_list_maker()
            s_avg = avg_list_maker()
            s_std = std_list_maker()
            sample_stat = s_min, s_max, s_avg, s_std

        elif type == "corr":
            if year[0] == year[1]:
                sample_stat = 1,1,1,1
            else:
                r_min = correlation_coe(min_list_maker(),min_list_maker(2))
                r_max = correlation_coe(max_list_maker(),max_list_maker(2))
                r_avg = correlation_coe(avg_list_maker(),avg_list_maker(2))
                r_std = correlation_coe(std_list_maker(),std_list_maker(2))
                sample_stat = r_min, r_max, r_avg, r_std

        return sample_stat

    except (FileNotFoundError,ZeroDivisionError,IndexError,ValueError,UnboundLocalError):
        if type == "stats":
            errorstats =  [],[],[],[]
        elif type == "corr":
            errorstats = 0.0,0.0,0.0,0.0
        else:
            return 
        return errorstats
    file_to_read.close()
print(main("sample_rainfall_data2.csv",[2019,2018],"corr"))
