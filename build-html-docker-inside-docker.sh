echo start building html
set -e
# copy to gh-pages
BUILD_DIR="gh-pages"
rm -rf -v $BUILD_DIR # else plantuml diagrams won't be rebuilt
# do not copy revealjs
mkdir -p $BUILD_DIR
cp -r -p -v asciidocs/images $BUILD_DIR/images/
cp -r -p -v asciidocs/plantuml $BUILD_DIR/plantuml/
cp -r -p -v asciidocs/themes $BUILD_DIR
cp -r -p -v asciidocs/docinfo.html $BUILD_DIR
cp -r -p -v asciidocs/.nojekyll $BUILD_DIR
cp -r -p -v asciidocs/index.adoc $BUILD_DIR
cp -r -p -v asciidocs/*.adoc $BUILD_DIR
cp -r -p -v asciidocs/ahitm $BUILD_DIR
cp -r -p -v asciidocs/ahif $BUILD_DIR
cp -r -p -v asciidocs/ahif/*.adoc $BUILD_DIR/ahif
cp -r -p -v asciidocs/ahitm/*.adoc $BUILD_DIR/ahitm
#uncomment it when you want to copy the source code into the gh-pages (for including source code into your document)
#cp -r -p -v src $BUILD_DIR

CURRENT_FOLDER=${PWD}
echo "pwd -> ${CURRENT_FOLDER}"
echo "adoc-folder->${CURRENT_FOLDER}/${BUILD_DIR}/*.adoc"
asciidoctor \
  -r asciidoctor-diagram \
  -a icons=font \
  -a experimental=true \
  -a source-highlighter=rouge \
  -a rouge-theme=github \
  -a rouge-linenums-mode=inline \
  -a docinfo=shared \
  -a imagesdir=images \
  -a toc=left \
  -a toclevels=2 \
  -a sectanchors=true \
  -a sectnums=true \
  -a favicon=themes/favicon.png \
  -a sourcedir=src/main/java \
  -b html5 \
  "${CURRENT_FOLDER}/${BUILD_DIR}/*.adoc"
rm -rf ./.asciidoctor
rm -v $BUILD_DIR/docinfo.html
rm -rf -v $BUILD_DIR/*.adoc
echo Creating html-docs in asciidocs in Docker finished ...
for d in $(find ${BUILD_DIR}/ -type d -maxdepth 1 -mindepth 1); do
  echo searching in ${d}
adoc=$(find ${d} -type f -name "*.adoc")
if [[ (-n $adoc) ]]
then
    BUILD_DIR="${d}"
    asciidoctor \
      -r asciidoctor-diagram \
      -a icons=font \
      -a experimental=true \
      -a source-highlighter=rouge \
      -a rouge-theme=github \
      -a rouge-linenums-mode=inline \
      -a docinfo=shared \
      -a imagesdir=images \
      -a toc=left \
      -a toclevels=2 \
      -a sectanchors=true \
      -a sectnums=true \
      -a favicon=themes/favicon.png \
      -a sourcedir=src/main/java \
      -b html5 \
      "${BUILD_DIR}/*.adoc"
    echo "${d} htmls created"
    rm -rf -v $BUILD_DIR/*.adoc
fi
done