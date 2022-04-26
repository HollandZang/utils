let color = '#125825';

chrome.runtime.onInstalled.addListener(() => {
  chrome.storage.sync.set({ color });
});

chrome.commands.onCommand.addListener((command) => {
  console.log(`Command: ${command}`);
});