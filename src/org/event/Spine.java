// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * <b>Represents stimuli with associated reflexes</b>
 * <div>When a stimulus occurs, appropriate reflexes are triggered</div>
 * @author wolfram77
 */
public class Spine extends ConcurrentHashMap<String, Set<Reflexive>> {
    
    // static data
    static Reflexive fallback = new Reflexive() {
        @Override
        public void on(String stimulus, Map args) {
            System.out.println("["+stimulus+"] : "+args);
            if(args.containsKey("err")) throw new RuntimeException((Throwable)args.get("err"));
        }
    };
    
    
    /**
     * <b>Convert a string from camel case to hyphen case</b>
     * @param str camel case string
     * @return hyphen case string
     */
    String _toHyphenCase(String str) {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c >= 'A' && c<='Z') {
                if(i > 0) s.append('-');
                s.append((char)(c-'A'+'a'));
            }
            else s.append(c);
        }
        return s.toString();
    }
    
    
    /**
     * <b>Add static / instance reflex methods of a class</b>
     * @param obj object containing reflex methods (null if static)
     * @param cls class containing reflex methods
     */
    private void _onClass(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String mthd = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!mthd.startsWith("on") || mthd.length()<=2 || isstatic != bestatic) continue;
            // save appropriately
            String stim = _toHyphenCase(mthd.substring(2));
            try {
                Reflex reflex = isstatic? new Reflex(cls, mthd) : new Reflex(obj, mthd);
                on(stim, reflex);
            }
            catch(ReflectiveOperationException e) {}
        }
    }
    
    
    /**
     * <b>Initialize reflex set of stimulus</b>
     * @param stimulus name of stimulus
     */
    void _initStimulus(String stimulus) {
        if(get(stimulus) != null) return;
        put(stimulus, Collections.newSetFromMap(new ConcurrentHashMap<Reflexive, Boolean>()));
    }
    
    
    /**
     * <b>Set fallback reflex, for stimulus with no reflex</b>
     * @param fallback fallback reflex
     */
    public static void fallback(Reflexive fallback) {
        Spine.fallback = fallback;
    }
    
    
    /**
     * <b>Get fallback reflex, for stimulus with no reflex</b>
     * @return fallback reflex
     */
    public static Reflexive fallback() {
        return fallback;
    }
    
    
    /**
     * <b>Create a spine</b>
     * <div>Associate reflexes to stimuli with {@code on(...)}</div>
     */
    public Spine() {
    }
    
    
    /**
     * <b>Create a spine from class</b>
     * <div>{@code on<stimulus>()} static methods are associated {@code <stimulus>} stimulus</div>
     * <div>Use {@code @Speed("slow")} annotation to indicate slow reflex methods</div>
     * @param cls class containing reflex methods
     */
    public Spine(Class cls) {
        _onClass(null, cls);
    }
    
    
    /**
     * <b>Create a spine from object</b>
     * <div>{@code on<stimulus>()} methods are associated {@code <stimulus>} stimulus</div>
     * <div>Use {@code @Reacts("slow")} annotation to indicate slow reflex methods</div>
     * @param obj object containing reflex methods
     */
    public Spine(Object obj) {
        _onClass(obj, obj.getClass());
    }
    
    
    /**
     * <b>Indicate a stimulus, causing reflexes to trigger</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Map args) {
        Set<Reflexive> s = get(stimulus);
        if(s == null || s.isEmpty()) fallback.on(stimulus, args);
        else for(Reflexive r : s)
            r.on(stimulus, args);
        return this;
    }
    
    
    /**
     * <b>Indicate a stimulus, causing reflexes to trigger</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Object... args) {
        Map margs = new HashMap();
        for(int i=1; i<args.length; i+=2)
            margs.put(args[i-1], args[i]);
        return is(stimulus, margs);
    }
    
    
    /**
     * <b>Set a reflex to trigger on a stimulus</b>
     * @param stimulus name of stimulus
     * @param reflex reflex to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Reflexive reflex) {
        _initStimulus(stimulus);
        get(stimulus).add(reflex);
        return this;
    }
    
    
    /**
     * <b>Set reflexes to trigger on a stimulus</b>
     * @param stimulus name of stimulus
     * @param reflexes reflexes to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Collection<Reflexive> reflexes) {
        _initStimulus(stimulus);
        get(stimulus).addAll(reflexes);
        return this;
    }
    
    
    /**
     * <b>Set a reflex to trigger on stimuli</b>
     * @param stimuli names of stimuli
     * @param reflex reflex to trigger
     * @return spine for chaining
     */
    public Spine on(Collection<String> stimuli, Reflexive reflex) {
        for(String stim : stimuli)
            on(stim, reflex);
        return this;
    }
    
    
    /**
     * <b>Set associated reflexes to trigger on stimuli</b>
     * @param assoc stimuli with associated reflexes to trigger
     * @return spine for chaining
     */
    public Spine on(Map<String, ? extends Collection<Reflexive>> assoc) {
        for(Map.Entry<String, ? extends Collection<Reflexive>> e : assoc.entrySet())
            on(e.getKey(), e.getValue());
        return this;
    }
    
    
    /**
     * <b>Turn off a reflex for a stimulus</b>
     * @param stimulus name of stimulus
     * @param reflex reflex to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Reflexive reflex) {
        Set<Reflexive> s = get(stimulus);
        if(s != null) s.remove(reflex);
        return this;
    }
    
    
    /**
     * <b>Turn off reflexes for a stimulus</b>
     * @param stimulus name of stimulus
     * @param reflexes reflexes to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Collection<Reflexive> reflexes) {
        Set<Reflexive> s = get(stimulus);
        if(s != null) s.removeAll(reflexes);
        return this;
    }
    
    
    /**
     * <b>Turn off a reflex for stimuli</b>
     * @param stimuli names of stimuli
     * @param reflex reflex to turn off
     * @return spine for chaining
     */
    public Spine off(Collection<String> stimuli, Reflexive reflex) {
        for(String stim : stimuli)
            off(stim, reflex);
        return this;
    }
    
    
    /**
     * <b>Turn off associated reflexes for stimuli</b>
     * @param assoc stimuli with associated reflexes to turn off
     * @return spine for chaining
     */
    public Spine off(Map<String, ? extends Collection<Reflexive>> assoc) {
        for(Map.Entry<String, ? extends Collection<Reflexive>> e : assoc.entrySet())
            off(e.getKey(), e.getValue());
        return this;
    }
    
    
    /**
     * <b>Turn off all reflexes for a stimulus</b>
     * @param stimulus name of stimulus
     * @return spine for chaining
     */
    public Spine off(String stimulus) {
        remove(stimulus);
        return this;
    }
    
    
    /**
     * <b>Turn off all reflexes for all stimuli</b>
     * @return spine for chaining
     */
    public Spine off() {
        clear();
        return this;
    }
}
