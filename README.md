| | | | |
|---:|:---:|:---:|:---:|
| [**main**](https://github.com/pmonks/ctac-bot/tree/main) | [![Build](https://github.com/pmonks/ctac-bot/workflows/build/badge.svg?branch=main)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Abuild) | [![Lint](https://github.com/pmonks/ctac-bot/workflows/lint/badge.svg?branch=main)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/pmonks/ctac-bot/workflows/dependencies/badge.svg?branch=main)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Adependencies) |
| [**dev**](https://github.com/pmonks/ctac-bot/tree/dev)  | [![Build](https://github.com/pmonks/ctac-bot/workflows/build/badge.svg?branch=dev)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Abuild) | [![Lint](https://github.com/pmonks/ctac-bot/workflows/lint/badge.svg?branch=dev)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/pmonks/ctac-bot/workflows/dependencies/badge.svg?branch=dev)](https://github.com/pmonks/ctac-bot/actions?query=workflow%3Adependencies) |

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

The `ctac-bot` source repository has two permanent branches: `main` and `dev`.  **All development must occur either in branch `dev`, or (preferably) in feature branches off of `dev`.**  All PRs must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

This model allows otherwise unrelated changes to be batched up in the `dev` branch, integration tested there, and then released en masse to the `main` branch.  The `main` branch is configured to auto-deploy to a production environment, and therefore that branch must only contain tested, functioning code.

## License

Copyright © 2021 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
