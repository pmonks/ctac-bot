| | | |
|---:|:---:|:---:|
| [**main**](https://github.com/pmonks/ctac-bot/tree/main) | [![CI](https://github.com/pmonks/ctac-bot/workflows/CI/badge.svg?branch=main)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3ACI+branch%3Amain) | [![Dependencies](https://github.com/pmonks/ctac-bot/workflows/dependencies/badge.svg?branch=main)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Adependencies+branch%3Amain) |
| [**dev**](https://github.com/pmonks/ctac-bot/tree/dev) | [![CI](https://github.com/pmonks/ctac-bot/workflows/CI/badge.svg?branch=dev)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3ACI+branch%3Adev) | [![Dependencies](https://github.com/pmonks/ctac-bot/workflows/dependencies/badge.svg?branch=dev)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Adependencies+branch%3Adev) |

[![Open Issues](https://img.shields.io/github/issues/pmonks/ctac-bot.svg)](https://github.com/pmonks/ctac-bot/issues)
[![License](https://img.shields.io/github/license/pmonks/ctac-bot.svg)](https://github.com/pmonks/ctac-bot/blob/main/LICENSE)

# ctac-bot

A small [Discord](https://discord.com/) bot specifically designed to support a single server. This repo is probably not of interest unless you're a developer who's in that server.

Please review the [privacy policy](https://github.com/pmonks/ctac-bot/blob/main/PRIVACY.md) before interacting with the bot.

## Contributor Information

[Contributing Guidelines](https://github.com/pmonks/ctac-bot/blob/main/.github/CONTRIBUTING.md)

[Bug Tracker](https://github.com/pmonks/ctac-bot/issues)

[Code of Conduct](https://github.com/pmonks/ctac-bot/blob/main/.github/CODE_OF_CONDUCT.md)

### Developer Workflow

This project uses the [git-flow branching strategy](https://nvie.com/posts/a-successful-git-branching-model/), with the caveat that the permanent branches are called `main` and `dev`, and any changes to the `main` branch are considered a release and auto-deployed (push to Heroku).

For this reason, **all development must occur either in branch `dev`, or (preferably) in temporary branches off of `dev`.**  All PRs from forked repos must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

## License

Copyright © 2021 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
