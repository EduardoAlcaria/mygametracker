from selenium import webdriver

from urllib.request import urlopen
from bs4 import BeautifulSoup

import sys

options = webdriver.ChromeOptions()
driver = webdriver.Chrome(options=options)

game_name = sys.argv[1].lower()

def title_verifier_via_steam(game_titles):
    count = 0
    for game_title in game_titles:
        if game_name in game_title.text.lower():
            return True
        count += 1
        if count == 5:
            break
def scrapper():
    driver.get(f"https://store.steampowered.com/search/?term={game_name.lower().replace(' ', '+').strip().rstrip()}")

    html = urlopen(driver.current_url)

    bs = BeautifulSoup(html, "html.parser")

    game_titles = bs.find_all("div", {"class": "col search_name ellipsis"})
    title_verified = title_verifier_via_steam(game_titles)
    if title_verified:
        print(f"{title_verified}\nsteam")
scrapper()
