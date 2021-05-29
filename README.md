This is a program that reads and processes data from TradeSkillMaster's .lua files for World of Warcraft into various forms (information about item sale/purchase sums/averages, searchable/filterable graphs, etc). The original purpose was to just colate data from multiple TSM.lua files, since I was multibox farming at the time, spread out accross 5-10 realms. I wanted to be able to easily see my total sales for the day, without having to manually look at each account. 

Now even though I'm not longer multibox farming, I still find it super interesting to be able to load up all my TSM data (back to when I started playing) and not have to deal with a single massive TSM.lua file that makes WoW take up a bunch more resources (and I like starting a new TSM.lua file each time I start a new run of crafting/enchanting shuffling - so all the in game price histories are current and accurate). It started as a simple personal project (thus the shitty development practices, little to no comments (currently), and very little flexability in some of the features), but I think it's become something that other people would find useful - especially once some of the features I have planned actually get developed. 

I'm making this repo public, in the hopes people can get some use out of it, even though it is far from a production ready release or anything like that, or that people might be able to help me get this into something that resembles a production ready program. 

Suggestions, comments, pull requests welcome.

Installation steps:
- Clone repository
- get a client id/secret and put it in apikey.txt with client ID, secret second, seperated by a colon (or run the program and there should be a GUI)
- Run the program from your java IDE of choice.

I've commited my IDNames.txt file. In that file is all the item id/name pairs that I've come accross in my TSM.lua files. The data is pulled from the blizzard api. Some may be outdated or just plain wrong. I've hardcoded some of the names as "UNKNOWN", to stop there being errors. You can delete the file and it'll be built it from scratch if you like, but that might take long time.

Hopefully someone out there get some use out of this, now or eventually.

Example of the graph: https://imgur.com/2iommnL

Partial To Do List:
- Graph overall
    - data point indicator
    - change zoom behavior
- Implement other windows/views
    - time period summary
        - Shows top sales/purchases by item of the day
        - total vendor/auction sale/purchases
        - net gold
- searching/filtering
    - custom search string interface
        - allow "or" logic
            - "sales from account 1 or purchases from account 2"
        - allow saved searches
            - so something like "bfa herbs" search is possible
        - "get current filter string" button
- change account to be based on parent folder
    - or allow it to be set after file choose gui?
- Maybe add some, you know, comments
