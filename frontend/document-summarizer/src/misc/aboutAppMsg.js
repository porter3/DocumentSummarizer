const variationCount = 30

export default 'This app generates summaries via the Natural Language Toolkit for Python, ' +
    'using existing sentences from the text. No new sentences are generated. The Python script scores ' + 
    'sentence importance based on a variety of factors and outputs all sentences that score above average. ' +
    "You'll notice that whenever the most verbose summary contains more than one sentence, a slider bar will " +
    'appear underneath the summary display. Moving the slider to the right will make the summary more succinct ' +
    'by filtering out sentences with lower scores. With this filtration, a summary can have up to ' +
    variationCount + ' variations.'