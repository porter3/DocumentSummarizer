export default function getLoaderMessage(sizeInBytes) {
    const sizeInKB = sizeInBytes / 1000
    let message = ''
    if (sizeInKB > 3000) {
        message = "Wow, that's a lot of text. This is going to take a few minutes."
    } else if (sizeInKB > 1000) {
        message = 'Oof, this is a decent amount of text to summarize. This might take a couple minutes.'
    } else if (sizeInKB > 350) {
        message = 'This file is a bit lengthy. It may take a moment to summarize.'
    }
    return message
}