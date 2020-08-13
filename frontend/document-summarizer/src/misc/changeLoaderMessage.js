import getLoaderMessage from './getLoaderMessage'

export default function changeLoaderMessage(setLoaderMessage, fileSizeInBytes) {
    setTimeout(() => { setLoaderMessage(getLoaderMessage(fileSizeInBytes)) }, 4000)
    setTimeout(() => setLoaderMessage('Still working...'), 30000)
    setTimeout(() => setLoaderMessage('STILL working...'), 60000)
    setTimeout(() => setLoaderMessage("I hope you're not getting too impatient. However, you're the one that wanted to summarize something this long."), 80000)
    setTimeout(() => setLoaderMessage('Still working...'), 100000)
}