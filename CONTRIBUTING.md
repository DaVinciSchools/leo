Contributing
============

Want to contribute? Great! First, read this page.

# Before You Contribute

**Project Leo** is distributed under the
[Apache License, Version 2.0](apache.org/licenses/LICENSE-2.0). See a local copy of the license in
[LICENSE](LICENSE)[^1].

# Filing Issues

File issues in the [Project Leo Issue Tracker](https://github.com/DaVinciSchools/leo/issues).

# Code Reviews

All submissions, including submissions by project members, require review. We use
[GitHub pull requests](https://docs.github.com/en/pull-requests/) for this purpose.

Tips for good pull requests:

* **Code Style**: We use various style guides to make files more readable and consistent. When in
  doubt, try to stay true to the existing code of the project. Run `mvn verify` to automatically
  format files before creating a PR. Styles that we use are:
    * [Google's Java style guide](https://google.github.io/styleguide/javaguide.html)
    * [Google's TypeScript style guide](https://google.github.io/styleguide/jsguide.html)
* **Commit Message**: Write a descriptive commit message. What problems are being solved and what
  are the consequences of the approach? Where and how did you test? Some good tips:
    * [5 Useful Tips For A Better Commit Message](http://robots.thoughtbot.com/5-useful-tips-for-a-better-commit-message)
    * [The Linux Kernel - Posting Patches](https://docs.kernel.org/process/5.Posting.html?highlight=patches#posting-patches)
* **Self Contained**: If your PR consists of multiple commits which are successive
  improvements / fixes to your first commit, consider squashing them into a single commit
  (`git rebase -i`) such that your PR is a single commit on top of the current HEAD. This make
  reviewing the code so much easier, and the history more readable.
* **Builds & Passes Tests**: Make sure that the code will build and pass tests (`mvn clean verify`)
  before creating a pull request and for each update.

[^1]: Copied from http://www.apache.org/licenses/LICENSE-2.0 on February 18, 2023.