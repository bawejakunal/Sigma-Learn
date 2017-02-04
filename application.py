#!flask/bin/python
from flask import Flask, request, render_template, g, redirect, Response, session, jsonify, url_for
from aylienapiclient import textapi
import math
import requests
import json

application = Flask(__name__)

textToGetContext = ""
finalFinalCon = ""
finalSummaryList = []

@application.route("/")
def hello():
    res = []
    #return render_template('home.html', orgText="The president of the National Assembly does it. The president of the Senate defends it. Dozens of rank-and-file parliamentarians do it, too. Hiring your spouse, son or sister in Frances Parliament is perfectly legal. So, with Franois Fillon, until recently Frances leading presidential candidate, in deep trouble for payments of nearly 1 million from the public payroll to his wife and children, many French politicians are asking What's the big deal? The answer has, belatedly, come roaring back from much of the countrys press and public They just don't get it. Penelopegate, a scandal named for Mr. Fillons wife, now threatens to sink the ambitions of a man who little more than a week ago seemed all but certain to become Frances next president. Fillon Scandal Indicts, Foremost, Frances Political Elite", summaryList=res)
    return render_template('home.html',
                           orgText=textToGetContext,
                           summaryList=finalSummaryList, concept=finalFinalCon)

@application.route("/devFest11/nytapi")
def nytapi():
    global finalFinalCon
    #url = 'nytapi.html?q='+finalFinalCon
    print finalFinalCon
    url = 'nytapi.html'
    #return "stop"
    return redirect(url_for(url, concept=finalFinalCon))
    #return render_template(url, concept=finalFinalCon)

'''
@application.route("/devFest11/getConcept")
def getConcept():
    global textToGetContext
    print "yo"
    text = textToGetContext
    #text = request.args.get['']
    print text
    client = textapi.Client("c1b78c7b", "8676a4a9b6d77080bf84f511a7665592")

    concepts = client.Concepts({"text": text})
    concept = []
    for uri, value in concepts['concepts'].items():
        sfs = map(lambda c: c['string'], value['surfaceForms'])
        concept.append(' '.join(sfs))
        print (' '.join(sfs))
    print (concept[0])
    return "hello"
    #res = []
    #return render_template('home.html', orgText="", summaryList=res)
'''
'''
@application.route("/devFest11/verify", method=['GET'])
def hel():
    return "Hello World!"
'''
@application.route('/devFest11/getSummary', methods=['POST'])
def getSummary():
    global textToGetContext
    global finalFinalCon
    global finalSummaryList
    content = request.get_json(silent=True)
    #content = request.form('text')
    print content
    title = "president"
    text = content['text'].encode('ascii', 'ignore').decode('ascii')
    print text
    size = int(math.ceil(len(text) / 200))
    if(size == 0):
        size = 2
    #print (size)
    client = textapi.Client("c1b78c7b", "8676a4a9b6d77080bf84f511a7665592")
    summary = client.Summarize({'title': title, 'text': text, 'sentences_number': size})
    res = []
    for sentence in summary['sentences']:
        res.append(sentence)
    print len(res)
    print res
    finalSummaryList = res
    textToGetContext = text
    concepts = client.Concepts({"text": text})
    concept = []
    for uri, value in concepts['concepts'].items():
        sfs = map(lambda c: c['string'], value['surfaceForms'])
        concept.append(' '.join(sfs))
        print (' '.join(sfs))
    finalConcept = concept[0]
    finalFinalCon = finalConcept
    print finalConcept
    return render_template('home.html', orgText=text, summaryList=res, concept=finalConcept)

@application.route('/devFest11/meaning', methods=['GET'])
def meaning():
    word = request.args.get('word')
    print "hello"
    print word
    url = 'https://wordsapiv1.p.mashape.com/words/' + word + '/definitions'
    response = requests.get(url, headers={
        "X-Mashape-Key": "zCJin20Cekmshjk93TynaAd80oO5p1y2Vm4jsnymJNxBHc9SZQ",
        "Accept": "application/json"
    })
    response = response.json()
    defs = dict()
    defStr = ""
    try:
        len_def = len(response['definitions'])
        if (len_def > 0):
            defStr = response['definitions'][0]['definition']
            #defs.append(response['definitions'][0]['definition'])
            if (len_def > 1):
                defStr = defStr + "; " + response['definitions'][1]['definition']
                #defs.append(response['definitions'][1]['definition'])
    except:
        defStr = defStr + "No meaning found"
        #defs.append("No meaning")
    print defs
    defs['meanings'] = defStr
    print defStr
    return jsonify(defs)

if __name__ == '__main__':
    application.run(debug=True,host="127.0.0.1")