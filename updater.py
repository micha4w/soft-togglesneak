import requests
import re
import textwrap as tw
import click

@click.command()
@click.argument('minecraft-version')
@click.option('--loader', default='forge', help='The Mod Loader to use [fabric, forge]')
@click.option('--mod-version', default=None, help='The Mod Version, if not specified reads version from last gradle.properties')
def update(minecraft_version, loader, mod_version):
    minecraft_version = re.sub(r'\.0(-.*)?$', r'\1', minecraft_version)
    if mod_version is None:
        with open('gradle.properties', 'r') as f:
            match = next(re.finditer(r'mod_version\s*=\s*(.*)', f.read()), None)
            mod_version = match.groups()[0]

    versions : dict = requests.get('https://linkieapi.shedaniel.me/api/versions/all').json()
    loader_versions : list = versions[loader]
    version : dict[str, dict] = next(filter(lambda v: v['version'] == minecraft_version, loader_versions), None)

    if version is None:
        print(f'Version not found: {minecraft_version}')
        exit(1)

    dependencies = {}
    for block in version['blocks'].values():
        for dependency in block['dependencies']:
            dependencies[dependency['name']] = dependency['version']

    gradle_properties = tw.dedent(f'''
        # This file was automatically generated by updater.py
        org.gradle.jvmargs=-Xmx1G

        # Mod Properties
        mod_version={mod_version}
        mod_group_id=net.micha4w
        mod_id=Soft_ToggleSneak

        minecraft_version={minecraft_version}
    ''')[1:]

    if loader.lower() == 'fabric':
        print(loader.lower())
        gradle_properties += tw.dedent(f'''
            # Fabric Properties
            yarn_mappings={dependencies['Yarn']}
            loader_version={dependencies['Fabric Loader']}

            # Dependencies
            api_version={dependencies['Fabric API']}
            cloth_config_version={dependencies['Cloth Config']}
            modmenu_version={dependencies['Mod Menu']}
        ''')

    elif loader.lower() == 'forge':
        mixin_release = requests.get('https://api.github.com/repos/SpongePowered/Mixin/releases/latest').json()
        mixin_version = mixin_release['tag_name'].split('/')[1]

        gradle_properties += tw.dedent(f'''
            # Forge Properties
            forge_version={dependencies['Forge']}

            # Dependencies
            mixin_version={mixin_version}
            cloth_config_version={dependencies['Cloth Config']}
        ''')

    else:
        print(f'Unknown Loader: {minecraft_version}')
        print('Should be one of: fabric, forge')
        exit(1)

    with open('gradle.properties', 'w') as f:
        f.write(gradle_properties)

if __name__ == '__main__':
    update()