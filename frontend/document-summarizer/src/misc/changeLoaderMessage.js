import getLoaderMessage from './getLoaderMessage'

export default function changeLoaderMessage(setLoaderMessage, fileSizeInBytes) {
    const timeoutA = setTimeout(() => { setLoaderMessage(getLoaderMessage(fileSizeInBytes)) }, 4000)
    const timeoutB = setTimeout(() => setLoaderMessage('Still working...'), 30000)
    const timeoutC = setTimeout(() => setLoaderMessage('STILL working...'), 60000)
    const timeoutD = setTimeout(() => setLoaderMessage("I hope you're not getting too impatient. However, you're the one that wanted to summarize something this long."), 80000)
    const timeoutE = setTimeout(() => setLoaderMessage('Still working...'), 100000)
    return [ timeoutA, timeoutB, timeoutC, timeoutD, timeoutE ]
}