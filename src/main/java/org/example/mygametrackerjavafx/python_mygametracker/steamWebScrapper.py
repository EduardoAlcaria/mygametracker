from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

from urllib.request import urlopen
from bs4 import BeautifulSoup

import sys
from time import sleep
import random

options = webdriver.ChromeOptions()
options.add_argument('--headless')
options.add_argument('--disable-gpu')
driver = webdriver.Chrome(options=options)
driver.get("https://store.steampowered.com/")

game_name = sys.argv[1].lower()


def randomsleep():
    rand = random.randint(0,3)
    sleep(rand)

def title_verifier(game_titles):
    count = 0
    for game_title in game_titles:
        if game_name.lower() in game_title.text.lower():
            return True
        count += 1
        if count == 5:
            break



def main():

    search_box = driver.find_element(By.XPATH, "//*[@id='store_nav_search_term']")

    randomsleep()


    search_box.click()

    randomsleep()

    for l in game_name:
        search_box.send_keys(l)
        sleep(0.1)

    sleep(1)

    search_box.send_keys(Keys.ENTER)

    randomsleep()

    html = urlopen(driver.current_url)

    randomsleep()

    bs = BeautifulSoup(html, "html.parser")

    game_titles = bs.find_all("div", {"class": "col search_name ellipsis"})


    print(title_verifier(game_titles))

    randomsleep()
main()


